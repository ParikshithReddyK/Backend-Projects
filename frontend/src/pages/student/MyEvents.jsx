import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";
import StatusBadge from "../../components/StatusBadge";

export default function MyEvents() {
  const [registrations, setRegistrations] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myEventRegistrations().then(setRegistrations).catch((e) => setError(e.message));
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">My Event Registrations</h1>
      <p className="text-sm text-ink-muted mb-8">Events you've signed up for.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {registrations === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {registrations && registrations.length === 0 && <EmptyState message="You haven't registered for any events yet." />}

      <div className="card divide-y divide-border">
        {registrations?.map((reg) => (
          <div key={reg.id} className="py-3 first:pt-0 last:pb-0 flex items-center justify-between">
            <p className="data text-sm">Event #{reg.eventId}</p>
            <StatusBadge status={reg.status} />
          </div>
        ))}
      </div>
    </div>
  );
}
