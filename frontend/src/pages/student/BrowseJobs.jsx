import { useEffect, useState } from "react";
import { api } from "../../api/client";
import StatusBadge from "../../components/StatusBadge";
import EmptyState from "../../components/EmptyState";

export default function BrowseJobs() {
  const [jobs, setJobs] = useState(null);
  const [applyingId, setApplyingId] = useState(null);
  const [appliedIds, setAppliedIds] = useState(new Set());
  const [error, setError] = useState("");

  useEffect(() => {
    api.browseJobs().then(setJobs).catch((e) => setError(e.message));
  }, []);

  async function handleApply(jobId) {
    setApplyingId(jobId);
    setError("");
    try {
      await api.applyToJob(jobId);
      setAppliedIds((prev) => new Set(prev).add(jobId));
    } catch (e) {
      setError(e.message);
    } finally {
      setApplyingId(null);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-1">Browse Jobs</h1>
      <p className="text-sm text-ink/50 mb-8">Open positions across campus departments.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      {jobs === null && <p className="text-sm text-ink/50">Loading…</p>}
      {jobs && jobs.length === 0 && <EmptyState message="No open jobs right now. Check back soon." />}

      <div className="space-y-4">
        {jobs?.map((job) => (
          <div key={job.id} className="card">
            <div className="flex items-start justify-between gap-4">
              <div>
                <h2 className="text-lg font-semibold">{job.title}</h2>
                <p className="text-sm text-ink/60">{job.department}</p>
              </div>
              <StatusBadge status={job.status} />
            </div>
            <p className="text-sm text-ink/70 mt-3">{job.description}</p>
            <div className="flex items-center justify-between mt-4 pt-4 border-t border-border">
              <p className="data text-sm">
                ${job.hourlyRate?.toFixed(2)}/hr · {job.hoursPerWeek} hrs/week
              </p>
              <button
                className="btn-primary"
                disabled={applyingId === job.id || appliedIds.has(job.id)}
                onClick={() => handleApply(job.id)}
              >
                {appliedIds.has(job.id)
                  ? "Applied"
                  : applyingId === job.id
                  ? "Applying…"
                  : "Apply"}
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
