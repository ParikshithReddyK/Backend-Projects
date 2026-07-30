import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

export default function MyPayroll() {
  const [records, setRecords] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myPayroll().then(setRecords).catch((e) => setError(e.message));
  }, []);

  const total = records?.reduce((sum, r) => sum + Number(r.totalPay || 0), 0) ?? 0;

  return (
    <div>
      <h1 className="text-2xl font-semibold mb-1">My Payroll</h1>
      <p className="text-sm text-ink/50 mb-8">Pay generated from your logged hours.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {records === null && <p className="text-sm text-ink/50">Loading…</p>}
      {records && records.length === 0 && <EmptyState message="No payroll records yet." />}

      {records && records.length > 0 && (
        <>
          <div className="card mb-4 flex items-center justify-between">
            <p className="text-sm text-ink/60">Total paid to date</p>
            <p className="data text-xl font-semibold text-success">${total.toFixed(2)}</p>
          </div>

          <div className="card overflow-hidden p-0">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-border text-xs uppercase tracking-wide text-ink/50">
                  <th className="text-left px-4 py-3 font-medium">Period</th>
                  <th className="text-right px-4 py-3 font-medium">Hours</th>
                  <th className="text-right px-4 py-3 font-medium">Rate</th>
                  <th className="text-right px-4 py-3 font-medium">Total</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-line">
                {records.map((r) => (
                  <tr key={r.id}>
                    <td className="px-4 py-3 data">
                      {r.periodStart} – {r.periodEnd}
                    </td>
                    <td className="px-4 py-3 text-right data">{r.totalHours}</td>
                    <td className="px-4 py-3 text-right data">${Number(r.hourlyRate).toFixed(2)}</td>
                    <td className="px-4 py-3 text-right data font-medium">
                      ${Number(r.totalPay).toFixed(2)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  );
}
