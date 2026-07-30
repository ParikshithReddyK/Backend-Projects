import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";
import StatusBadge from "../../components/StatusBadge";

const initialForm = { jobId: "", startDate: "", endDate: "", reason: "" };

export default function MyLeaves() {
  const [leaves, setLeaves] = useState(null);
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  function refresh() {
    api.myLeaves().then(setLeaves).catch((e) => setError(e.message));
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
      await api.requestLeave({ ...form, jobId: Number(form.jobId) });
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
      <h1 className="text-2xl font-bold mb-1">My Leave Requests</h1>
      <p className="text-sm text-ink-muted mb-8">Request time off from a job you're working.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      <form onSubmit={handleSubmit} className="card mb-6 space-y-4">
        <div>
          <label className="label">Job ID</label>
          <input
            type="number"
            required
            className="input"
            value={form.jobId}
            onChange={(e) => update("jobId", e.target.value)}
            placeholder="1"
          />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="label">Start date</label>
            <input
              type="date"
              required
              className="input"
              value={form.startDate}
              onChange={(e) => update("startDate", e.target.value)}
            />
          </div>
          <div>
            <label className="label">End date</label>
            <input
              type="date"
              required
              className="input"
              value={form.endDate}
              onChange={(e) => update("endDate", e.target.value)}
            />
          </div>
        </div>
        <div>
          <label className="label">Reason</label>
          <textarea
            required
            rows={3}
            className="input"
            value={form.reason}
            onChange={(e) => update("reason", e.target.value)}
            placeholder="Briefly explain why you need this time off…"
          />
        </div>
        <button type="submit" disabled={submitting} className="btn-primary">
          {submitting ? "Submitting…" : "Request leave"}
        </button>
      </form>

      {leaves === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {leaves && leaves.length === 0 && <EmptyState message="No leave requests yet." />}
      {leaves && leaves.length > 0 && (
        <div className="card divide-y divide-border">
          {leaves.map((leave) => (
            <div key={leave.id} className="py-3 first:pt-0 last:pb-0 flex items-center justify-between">
              <div>
                <p className="data text-sm">{leave.startDate} → {leave.endDate}</p>
                <p className="text-xs text-ink-faint">{leave.reason}</p>
              </div>
              <StatusBadge status={leave.status} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
