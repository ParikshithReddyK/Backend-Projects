import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

export default function MyReviews() {
  const [reviews, setReviews] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myReviews().then(setReviews).catch((e) => setError(e.message));
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">My Performance Reviews</h1>
      <p className="text-sm text-ink-muted mb-8">Feedback from your supervisors.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {reviews === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {reviews && reviews.length === 0 && <EmptyState message="No reviews yet." />}

      <div className="space-y-3">
        {reviews?.map((r) => (
          <div key={r.id} className="card">
            <div className="flex items-center justify-between mb-2">
              <p className="data text-xs text-ink-faint">
                {r.reviewPeriodStart} – {r.reviewPeriodEnd}
              </p>
              <p className="data text-lg font-bold text-primary">{r.rating}/5</p>
            </div>
            <p className="text-sm">{r.feedback}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
