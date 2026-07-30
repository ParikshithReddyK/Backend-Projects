import { useEffect, useState } from "react";
import { api } from "../../api/client";
import StatusBadge from "../../components/StatusBadge";
import EmptyState from "../../components/EmptyState";

export default function LeaveRequests() {
  const [jobs, setJobs] = useState(null);
  const [selectedJob, setSelectedJob] = useState(null);
  const [leaves, setLeaves] = useState(null);
  const [updatingId, setUpdatingId] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myJobs().then(setJobs).catch((e) => setError(e.message));
  }, []);

  function openJob(job) {
    setSelectedJob(job);
    setLeaves(null);
    api.leavesForJob(job.id).then(setLeaves).catch((e) => setError(e.message));
  }

  async function updateStatus(id, status) {
    setUpdatingId(id);
    setError("");
    try {
      const updated = await api.updateLeaveStatus(id, status);
      setLeaves((prev) => prev.map((l) => (l.id === id ? updated : l)));
    } catch (e) {
      setError(e.message);
    } finally {
      setUpdatingId(null);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Leave Requests</h1>
      <p className="text-sm text-ink-muted mb-8">Review time-off requests for jobs you manage.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      <div className="grid grid-cols-2 gap-6">
        <div>
          <p className="label mb-2">Your jobs</p>
          {jobs === null && <p className="text-sm text-ink-muted">Loading…</p>}
          {jobs && jobs.length === 0 && <EmptyState message="You haven't posted any jobs yet." />}
          <div className="space-y-2">
            {jobs?.map((job) => (
              <button
                key={job.id}
                onClick={() => openJob(job)}
                className={`w-full text-left card py-3 ${selectedJob?.id === job.id ? "border-primary" : ""}`}
              >
                <p className="text-sm font-medium">{job.title}</p>
              </button>
            ))}
          </div>
        </div>

        <div>
          <p className="label mb-2">Leave requests</p>
          {!selectedJob && <EmptyState message="Select a job to view leave requests." />}
          {selectedJob && leaves === null && <p className="text-sm text-ink-muted">Loading…</p>}
          {selectedJob && leaves?.length === 0 && <EmptyState message="No leave requests for this job." />}

          <div className="space-y-2">
            {leaves?.map((leave) => (
              <div key={leave.id} className="card py-3">
                <div className="flex items-center justify-between mb-2">
                  <p className="data text-xs">{leave.startDate} → {leave.endDate}</p>
                  <StatusBadge status={leave.status} />
                </div>
                <p className="text-sm text-ink-muted mb-2">{leave.reason}</p>
                {leave.status === "PENDING" && (
                  <div className="flex gap-2">
                    <button
                      className="btn-primary flex-1 text-xs py-1.5"
                      disabled={updatingId === leave.id}
                      onClick={() => updateStatus(leave.id, "APPROVED")}
                    >
                      Approve
                    </button>
                    <button
                      className="btn-secondary flex-1 text-xs py-1.5"
                      disabled={updatingId === leave.id}
                      onClick={() => updateStatus(leave.id, "REJECTED")}
                    >
                      Reject
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
