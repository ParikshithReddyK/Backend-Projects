import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

const initialForm = { title: "", description: "", category: "" };

export default function ManageTraining() {
  const [modules, setModules] = useState(null);
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  function refresh() {
    api.browseModules().then(setModules).catch((e) => setError(e.message));
  }

  useEffect(refresh, []);

  function update(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    try {
      await api.createModule(form);
      setForm(initialForm);
      refresh();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Training Modules</h1>
      <p className="text-sm text-ink-muted mb-8">Create training and compliance modules for students.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      <form onSubmit={handleSubmit} className="card mb-6 space-y-4 max-w-xl">
        <div>
          <label className="label">Title</label>
          <input required className="input" value={form.title} onChange={(e) => update("title", e.target.value)} placeholder="Workplace Safety Basics" />
        </div>
        <div>
          <label className="label">Category</label>
          <input required className="input" value={form.category} onChange={(e) => update("category", e.target.value)} placeholder="Compliance" />
        </div>
        <div>
          <label className="label">Description</label>
          <textarea required rows={3} className="input" value={form.description} onChange={(e) => update("description", e.target.value)} />
        </div>
        <button type="submit" disabled={submitting} className="btn-primary">
          {submitting ? "Creating…" : "Create module"}
        </button>
      </form>

      <p className="text-sm font-semibold mb-3">All modules</p>
      {modules === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {modules && modules.length === 0 && <EmptyState message="No modules created yet." />}
      <div className="space-y-3">
        {modules?.map((mod) => (
          <div key={mod.id} className="card">
            <p className="text-sm font-medium">{mod.title}</p>
            <p className="text-xs text-ink-faint">{mod.category}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
