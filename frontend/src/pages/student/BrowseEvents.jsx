import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

function formatDateTime(iso) {
  if (!iso) return "—";
  return new Date(iso).toLocaleString(undefined, {
    month: "short", day: "numeric", hour: "numeric", minute: "2-digit",
  });
}

export default function BrowseEvents() {
  const [events, setEvents] = useState(null);
  const [registeringId, setRegisteringId] = useState(null);
  const [registeredIds, setRegisteredIds] = useState(new Set());
  const [error, setError] = useState("");

  useEffect(() => {
    api.browseEvents().then(setEvents).catch((e) => setError(e.message));
  }, []);

  async function handleRegister(eventId) {
    setRegisteringId(eventId);
    setError("");
    try {
      await api.registerForEvent(eventId);
      setRegisteredIds((prev) => new Set(prev).add(eventId));
    } catch (e) {
      setError(e.message);
    } finally {
      setRegisteringId(null);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Campus Events</h1>
      <p className="text-sm text-ink-muted mb-8">Volunteering and campus events open for registration.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {events === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {events && events.length === 0 && <EmptyState message="No events scheduled right now." />}

      <div className="space-y-4">
        {events?.map((event) => (
          <div key={event.id} className="card">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-sm font-semibold">{event.title}</p>
                <p className="text-xs text-ink-faint">{event.location}</p>
              </div>
              <p className="data text-xs text-ink-muted">{formatDateTime(event.eventDate)}</p>
            </div>
            <p className="text-sm text-ink-muted mt-3">{event.description}</p>
            <div className="flex items-center justify-between mt-4 pt-4 border-t border-border">
              <p className="data text-xs text-ink-faint">
                {event.registeredCount}{event.maxParticipants ? ` / ${event.maxParticipants}` : ""} registered
              </p>
              <button
                className="btn-primary text-xs py-1.5"
                disabled={registeringId === event.id || registeredIds.has(event.id)}
                onClick={() => handleRegister(event.id)}
              >
                {registeredIds.has(event.id) ? "Registered" : registeringId === event.id ? "Registering…" : "Register"}
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
