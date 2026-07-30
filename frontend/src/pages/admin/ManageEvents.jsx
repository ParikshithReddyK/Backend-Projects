import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

const initialForm = { title: "", description: "", eventDate: "", location: "", maxParticipants: "" };

function formatDateTime(iso) {
  if (!iso) return "—";
  return new Date(iso).toLocaleString(undefined, {
    month: "short", day: "numeric", hour: "numeric", minute: "2-digit",
  });
}

export default function ManageEvents() {
  const [form, setForm] = useState(initialForm);
  const [events, setEvents] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  function refresh() {
    api.browseEvents().then(setEvents).catch((e) => setError(e.message));
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
      await api.createEvent({
        ...form,
        maxParticipants: form.maxParticipants ? Number(form.maxParticipants) : null,
      });
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
      <h1 className="text-2xl font-bold mb-1">Manage Events</h1>
      <p className="text-sm text-ink-muted mb-8">Create campus events and volunteering opportunities.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      <form onSubmit={handleSubmit} className="card mb-6 space-y-4 max-w-xl">
        <div>
          <label className="label">Title</label>
          <input required className="input" value={form.title} onChange={(e) => update("title", e.target.value)} placeholder="Volunteer Day" />
        </div>
        <div>
          <label className="label">Location</label>
          <input required className="input" value={form.location} onChange={(e) => update("location", e.target.value)} placeholder="Main Quad" />
        </div>
        <div>
          <label className="label">Description</label>
          <textarea required rows={3} className="input" value={form.description} onChange={(e) => update("description", e.target.value)} />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="label">Date & time</label>
            <input type="datetime-local" required className="input" value={form.eventDate} onChange={(e) => update("eventDate", e.target.value)} />
          </div>
          <div>
            <label className="label">Max participants (optional)</label>
            <input type="number" min="1" className="input" value={form.maxParticipants} onChange={(e) => update("maxParticipants", e.target.value)} />
          </div>
        </div>
        <button type="submit" disabled={submitting} className="btn-primary">
          {submitting ? "Creating…" : "Create event"}
        </button>
      </form>

      <p className="text-sm font-semibold mb-3">All events</p>
      {events === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {events && events.length === 0 && <EmptyState message="No events created yet." />}
      <div className="space-y-3">
        {events?.map((ev) => (
          <div key={ev.id} className="card flex items-center justify-between">
            <div>
              <p className="text-sm font-medium">{ev.title}</p>
              <p className="data text-xs text-ink-faint">{formatDateTime(ev.eventDate)} · {ev.location}</p>
            </div>
            <p className="data text-sm">
              {ev.registeredCount}{ev.maxParticipants ? ` / ${ev.maxParticipants}` : ""}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}
