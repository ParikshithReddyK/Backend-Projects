import { useEffect, useState } from "react";
import { api } from "../../api/client";
import StatusBadge from "../../components/StatusBadge";
import EmptyState from "../../components/EmptyState";

export default function ManageJobs() {
  const [jobs, setJobs] = useState(null);
  const [selectedJob, setSelectedJob] = useState(null);
  const [applications, setApplications] = useState(null);
  const [error, setError] = useState("");
  const [updatingId, setUpdatingId] = useState(null);

  useEffect(() => {
    api.myJobs().then(setJobs).catch((e) => setError(e.message));
  }, []);

  function openJob(job) {
    setSelectedJob(job);
    setApplications(null);
    api.applicationsForJob(job.id).then(setApplications).catch((e) => setError(e.message));
  }

  async function updateStatus(appId, status) {
    setUpdatingId(appId);
    setError("");
    try {
      const updated = await api.updateApplicationStatus(appId, status);
      setApplications((prev) => prev.map((a) => (a.id === appId ? updated : a)));
    } catch (e) {
      setError(e.message);
    } finally {
      setUpdatingId(null);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-1">Manage Jobs</h1>
      <p className="text-sm text-ink/50 mb-8">Review applicants for jobs you've posted.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      <div className="grid grid-cols-2 gap-6">
        <div>
          <p className="label mb-2">Your jobs</p>
          {jobs === null && <p className="text-sm text-ink/50">Loading…</p>}
          {jobs && jobs.length === 0 && <EmptyState message="You haven't posted any jobs yet." />}
          <div className="space-y-2">
            {jobs?.map((job) => (
              <button
                key={job.id}
                onClick={() => openJob(job)}
                className={`w-full text-left card py-3 ${
                  selectedJob?.id === job.id ? "border-primary" : ""
                }`}
              >
                <div className="flex items-center justify-between">
                  <p className="text-sm font-medium">{job.title}</p>
                  <StatusBadge status={job.status} />
                </div>
              </button>
            ))}
          </div>
        </div>

        <div>
          <p className="label mb-2">Applicants</p>
          {!selectedJob && <EmptyState message="Select a job to view its applicants." />}
          {selectedJob && applications === null && <p className="text-sm text-ink/50">Loading…</p>}
          {selectedJob && applications?.length === 0 && (
            <EmptyState message="No applications for this job yet." />
          )}

          <div className="space-y-2">
            {applications?.map((app) => (
              <div key={app.id} className="card py-3">
                <div className="flex items-center justify-between mb-2">
                  <p className="text-sm font-medium data">Student #{app.studentId}</p>
                  <StatusBadge status={app.status} />
                </div>
                {app.status === "PENDING" && (
                  <div className="flex gap-2">
                    <button
                      className="btn-primary flex-1 text-xs py-1.5"
                      disabled={updatingId === app.id}
                      onClick={() => updateStatus(app.id, "ACCEPTED")}
                    >
                      Accept
                    </button>
                    <button
                      className="btn-secondary flex-1 text-xs py-1.5"
                      disabled={updatingId === app.id}
                      onClick={() => updateStatus(app.id, "REJECTED")}
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
