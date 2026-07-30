import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

const initialForm = { studentId: "", rating: "5", feedback: "", reviewPeriodStart: "", reviewPeriodEnd: "" };

export default function GiveReview() {
  const [jobs, setJobs] = useState(null);
  const [selectedJob, setSelectedJob] = useState(null);
  const [applicants, setApplicants] = useState(null);
  const [form, setForm] = useState(initialForm);
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myJobs().then(setJobs).catch((e) => setError(e.message));
  }, []);

  function openJob(job) {
    setSelectedJob(job);
    setApplicants(null);
    setSuccess(false);
    api
      .applicationsForJob(job.id)
      .then((apps) => setApplicants(apps.filter((a) => a.status === "ACCEPTED")))
      .catch((e) => setError(e.message));
  }

  function update(field, value) {
    setForm((prev) => ({ ...prev, [field]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    setSuccess(false);
    try {
      await api.createReview({
        ...form,
        jobId: selectedJob.id,
        studentId: Number(form.studentId),
        rating: Number(form.rating),
      });
      setForm(initialForm);
      setSuccess(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Give a Review</h1>
      <p className="text-sm text-ink-muted mb-8">Rate and provide feedback for students you supervise.</p>

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
          {!selectedJob && <EmptyState message="Select a job to review its students." />}
          {selectedJob && (
            <form onSubmit={handleSubmit} className="card space-y-4">
              <div>
                <label className="label">Student</label>
                <select
                  required
                  className="input"
                  value={form.studentId}
                  onChange={(e) => update("studentId", e.target.value)}
                >
                  <option value="">Select a student…</option>
                  {applicants?.map((a) => (
                    <option key={a.studentId} value={a.studentId}>Student #{a.studentId}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="label">Rating (1-5)</label>
                <input
                  type="number"
                  min="1"
                  max="5"
                  required
                  className="input"
                  value={form.rating}
                  onChange={(e) => update("rating", e.target.value)}
                />
              </div>
              <div>
                <label className="label">Feedback</label>
                <textarea
                  required
                  rows={3}
                  className="input"
                  value={form.feedback}
                  onChange={(e) => update("feedback", e.target.value)}
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="label">Period start</label>
                  <input
                    type="date"
                    required
                    className="input"
                    value={form.reviewPeriodStart}
                    onChange={(e) => update("reviewPeriodStart", e.target.value)}
                  />
                </div>
                <div>
                  <label className="label">Period end</label>
                  <input
                    type="date"
                    required
                    className="input"
                    value={form.reviewPeriodEnd}
                    onChange={(e) => update("reviewPeriodEnd", e.target.value)}
                  />
                </div>
              </div>
              {success && <p className="text-sm text-success">Review submitted.</p>}
              <button type="submit" disabled={submitting} className="btn-primary">
                {submitting ? "Submitting…" : "Submit review"}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}
