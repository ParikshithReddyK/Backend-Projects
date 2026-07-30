import { useEffect, useState } from "react";
import { api } from "../../api/client";
import StatusBadge from "../../components/StatusBadge";
import EmptyState from "../../components/EmptyState";

export default function MyApplications() {
  const [applications, setApplications] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myApplications().then(setApplications).catch((e) => setError(e.message));
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-1">My Applications</h1>
      <p className="text-sm text-ink/50 mb-8">Track the status of jobs you've applied to.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {applications === null && <p className="text-sm text-ink/50">Loading…</p>}
      {applications && applications.length === 0 && (
        <EmptyState message="You haven't applied to any jobs yet." />
      )}

      <div className="card divide-y divide-line">
        {applications?.map((app) => (
          <div key={app.id} className="py-3 first:pt-0 last:pb-0 flex items-center justify-between">
            <div>
              <p className="text-sm font-medium">Job #{app.jobId}</p>
              <p className="data text-xs text-ink/50">Application #{app.id}</p>
            </div>
            <StatusBadge status={app.status} />
          </div>
        ))}
      </div>
    </div>
  );
}
