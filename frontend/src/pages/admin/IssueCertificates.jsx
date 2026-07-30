import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

export default function IssueCertificates() {
  const [jobs, setJobs] = useState(null);
  const [selectedJob, setSelectedJob] = useState(null);
  const [applicants, setApplicants] = useState(null);
  const [studentId, setStudentId] = useState("");
  const [title, setTitle] = useState("Certificate of Service");
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myJobs().then(setJobs).catch((e) => setError(e.message));
  }, []);

  function openJob(job) {
    setSelectedJob(job);
    setApplicants(null);
    setSuccess(null);
    api
      .applicationsForJob(job.id)
      .then((apps) => setApplicants(apps.filter((a) => a.status === "ACCEPTED")))
      .catch((e) => setError(e.message));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    setSuccess(null);
    try {
      const cert = await api.issueCertificate({
        studentId: Number(studentId),
        jobId: selectedJob.id,
        title,
      });
      setSuccess(cert);
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Issue a Certificate</h1>
      <p className="text-sm text-ink-muted mb-8">
        Generate a certificate based on a student's logged hours.
      </p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      <div className="grid grid-cols-2 gap-6">
        <div>
          <p className="label mb-2">Your jobs</p>
          {jobs === null && <p className="text-sm text-ink-muted">Loading…</p>}
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
          {!selectedJob && <EmptyState message="Select a job to issue a certificate." />}
          {selectedJob && (
            <form onSubmit={handleSubmit} className="card space-y-4">
              <div>
                <label className="label">Student</label>
                <select
                  required
                  className="input"
                  value={studentId}
                  onChange={(e) => setStudentId(e.target.value)}
                >
                  <option value="">Select a student…</option>
                  {applicants?.map((a) => (
                    <option key={a.studentId} value={a.studentId}>Student #{a.studentId}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="label">Certificate title</label>
                <input
                  required
                  className="input"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />
              </div>
              {success && (
                <p className="text-sm text-success">
                  Issued {success.certificateNumber} — {success.totalHours} hrs logged.
                </p>
              )}
              <button type="submit" disabled={submitting} className="btn-primary">
                {submitting ? "Issuing…" : "Issue certificate"}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}
