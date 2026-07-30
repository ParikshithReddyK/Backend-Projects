import { useState } from "react";
import { api } from "../../api/client";

const initialForm = {
  title: "",
  description: "",
  department: "",
  hourlyRate: "",
  hoursPerWeek: "",
};

export default function PostJob() {
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  function update(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setSuccess(false);
    setLoading(true);
    try {
      await api.createJob({
        ...form,
        hourlyRate: Number(form.hourlyRate),
        hoursPerWeek: Number(form.hoursPerWeek),
      });
      setForm(initialForm);
      setSuccess(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-1">Post a Job</h1>
      <p className="text-sm text-ink/50 mb-8">Create a new work-study position.</p>

      <form onSubmit={handleSubmit} className="card space-y-4 max-w-xl">
        <div>
          <label className="label" htmlFor="title">Title</label>
          <input
            id="title"
            required
            className="input"
            value={form.title}
            onChange={(e) => update("title", e.target.value)}
            placeholder="Library Assistant"
          />
        </div>

        <div>
          <label className="label" htmlFor="department">Department</label>
          <input
            id="department"
            required
            className="input"
            value={form.department}
            onChange={(e) => update("department", e.target.value)}
            placeholder="Campus Library"
          />
        </div>

        <div>
          <label className="label" htmlFor="description">Description</label>
          <textarea
            id="description"
            required
            rows={4}
            className="input"
            value={form.description}
            onChange={(e) => update("description", e.target.value)}
            placeholder="What the role involves day to day…"
          />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="label" htmlFor="hourlyRate">Hourly rate ($)</label>
            <input
              id="hourlyRate"
              type="number"
              step="0.01"
              min="0"
              required
              className="input"
              value={form.hourlyRate}
              onChange={(e) => update("hourlyRate", e.target.value)}
              placeholder="14.00"
            />
          </div>
          <div>
            <label className="label" htmlFor="hoursPerWeek">Hours / week</label>
            <input
              id="hoursPerWeek"
              type="number"
              min="1"
              required
              className="input"
              value={form.hoursPerWeek}
              onChange={(e) => update("hoursPerWeek", e.target.value)}
              placeholder="8"
            />
          </div>
        </div>

        {error && <p className="text-sm text-danger">{error}</p>}
        {success && <p className="text-sm text-success">Job posted successfully.</p>}

        <button type="submit" disabled={loading} className="btn-primary">
          {loading ? "Posting…" : "Post job"}
        </button>
      </form>
    </div>
  );
}
