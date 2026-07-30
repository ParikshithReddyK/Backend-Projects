import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";
import StatusBadge from "../../components/StatusBadge";

export default function MyEquipment() {
  const [assignments, setAssignments] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myEquipmentAssignments().then(setAssignments).catch((e) => setError(e.message));
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">My Equipment</h1>
      <p className="text-sm text-ink-muted mb-8">Equipment currently or previously assigned to you.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {assignments === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {assignments && assignments.length === 0 && <EmptyState message="No equipment assigned yet." />}

      <div className="card divide-y divide-border">
        {assignments?.map((a) => (
          <div key={a.id} className="py-3 first:pt-0 last:pb-0 flex items-center justify-between">
            <p className="data text-sm">Equipment #{a.equipmentId}</p>
            <StatusBadge status={a.status} />
          </div>
        ))}
      </div>
    </div>
  );
}
