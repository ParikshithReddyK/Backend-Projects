import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../../api/client";
import StatCard from "../../components/StatCard";

function formatDateTime(iso) {
  if (!iso) return "—";
  return new Date(iso).toLocaleString(undefined, {
    month: "short",
    day: "numeric",
    hour: "numeric",
    minute: "2-digit",
  });
}

export default function StudentDashboard() {
  const [applications, setApplications] = useState(null);
  const [shifts, setShifts] = useState(null);
  const [payroll, setPayroll] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([api.myApplications(), api.myShifts(), api.myPayroll()])
      .then(([apps, sh, pay]) => {
        setApplications(apps);
        setShifts(sh);
        setPayroll(pay);
      })
      .catch((e) => setError(e.message));
  }, []);

  const totalPaid = payroll?.reduce((sum, r) => sum + Number(r.totalPay || 0), 0) ?? 0;
  const upcomingShift = shifts
    ?.filter((s) => s.status === "SCHEDULED")
    .sort((a, b) => new Date(a.shiftStart) - new Date(b.shiftStart))[0];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Dashboard</h1>
      <p className="text-sm text-ink-muted mb-8">Your work-study activity at a glance.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      <div className="grid grid-cols-3 gap-4 mb-6">
        <StatCard label="Applications submitted" value={applications?.length ?? "—"} />
        <StatCard label="Scheduled shifts" value={shifts?.length ?? "—"} />
        <StatCard label="Total paid to date" value={`$${totalPaid.toFixed(2)}`} />
      </div>

      <div className="card">
        <p className="text-sm font-semibold mb-3">Next shift</p>
        {upcomingShift ? (
          <p className="data text-sm">
            {formatDateTime(upcomingShift.shiftStart)} → {formatDateTime(upcomingShift.shiftEnd)}
          </p>
        ) : (
          <p className="text-sm text-ink-muted">
            No upcoming shifts.{" "}
            <Link to="/jobs" className="text-primary font-medium hover:underline">
              Browse open jobs
            </Link>
            .
          </p>
        )}
      </div>
    </div>
  );
}
