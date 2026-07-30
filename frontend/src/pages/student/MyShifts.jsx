import { useEffect, useState } from "react";
import { api } from "../../api/client";
import StatusBadge from "../../components/StatusBadge";
import EmptyState from "../../components/EmptyState";

function formatDateTime(iso) {
  if (!iso) return "—";
  return new Date(iso).toLocaleString(undefined, {
    month: "short",
    day: "numeric",
    hour: "numeric",
    minute: "2-digit",
  });
}

export default function MyShifts() {
  const [shifts, setShifts] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myShifts().then(setShifts).catch((e) => setError(e.message));
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-1">My Shifts</h1>
      <p className="text-sm text-ink/50 mb-8">Scheduled work sessions.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {shifts === null && <p className="text-sm text-ink/50">Loading…</p>}
      {shifts && shifts.length === 0 && <EmptyState message="No shifts scheduled yet." />}

      <div className="card divide-y divide-line">
        {shifts?.map((shift) => (
          <div key={shift.id} className="py-3 first:pt-0 last:pb-0 flex items-center justify-between">
            <div>
              <p className="text-sm font-medium data">
                {formatDateTime(shift.shiftStart)} → {formatDateTime(shift.shiftEnd)}
              </p>
              <p className="text-xs text-ink/50">Job #{shift.jobId}</p>
            </div>
            <StatusBadge status={shift.status} />
          </div>
        ))}
      </div>
    </div>
  );
}
