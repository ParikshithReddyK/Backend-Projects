import { useEffect, useState } from "react";
import { api } from "../../api/client";
import StatCard from "../../components/StatCard";
import StatusBadge from "../../components/StatusBadge";

export default function AdminDashboard() {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.jobsOverview().then(setData).catch((e) => setError(e.message));
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Dashboard</h1>
      <p className="text-sm text-ink-muted mb-8">
        A live overview of jobs and applications across campus.
      </p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {!data && !error && <p className="text-sm text-ink-muted">Loading…</p>}

      {data && (
        <>
          <div className="grid grid-cols-3 gap-4 mb-6">
            <StatCard label="Total jobs posted" value={data.totalJobs} />
            <StatCard label="Currently open" value={data.openJobs} />
            <StatCard label="Total applications" value={data.totalApplications} />
          </div>

          <div className="card">
            <p className="text-sm font-semibold mb-4">Applications by status</p>
            {Object.keys(data.applicationsByStatus || {}).length === 0 ? (
              <p className="text-sm text-ink-muted">No applications yet.</p>
            ) : (
              <div className="space-y-3">
                {Object.entries(data.applicationsByStatus).map(([status, count]) => (
                  <div key={status} className="flex items-center justify-between">
                    <StatusBadge status={status} />
                    <span className="data text-sm font-medium">{count}</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}
