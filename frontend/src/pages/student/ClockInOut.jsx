import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

function formatDateTime(iso) {
  if (!iso) return "—";
  return new Date(iso).toLocaleString(undefined, {
    month: "short", day: "numeric", hour: "numeric", minute: "2-digit",
  });
}

export default function ClockInOut() {
  const [shifts, setShifts] = useState(null);
  const [attendance, setAttendance] = useState(null);
  const [error, setError] = useState("");
  const [busy, setBusy] = useState(false);

  function refresh() {
    api.myShifts().then(setShifts).catch((e) => setError(e.message));
    api.myAttendance().then(setAttendance).catch((e) => setError(e.message));
  }

  useEffect(refresh, []);

  const openRecord = attendance?.find((a) => !a.clockOut);
  const scheduledShifts = shifts?.filter((s) => s.status === "SCHEDULED") ?? [];

  async function handleClockIn(shiftId) {
    setBusy(true);
    setError("");
    try {
      await api.clockIn(shiftId);
      refresh();
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  async function handleClockOut() {
    setBusy(true);
    setError("");
    try {
      await api.clockOut();
      refresh();
    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Clock In / Out</h1>
      <p className="text-sm text-ink-muted mb-8">Log your work hours against a scheduled shift.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      {openRecord ? (
        <div className="card mb-6 flex items-center justify-between">
          <div>
            <p className="text-sm font-semibold">Currently clocked in</p>
            <p className="data text-xs text-ink-muted">Since {formatDateTime(openRecord.clockIn)}</p>
          </div>
          <button className="btn-primary" disabled={busy} onClick={handleClockOut}>
            {busy ? "Clocking out…" : "Clock out"}
          </button>
        </div>
      ) : (
        <div className="card mb-6">
          <p className="text-sm font-semibold mb-3">Clock in to a scheduled shift</p>
          {scheduledShifts.length === 0 ? (
            <p className="text-sm text-ink-muted">No scheduled shifts available.</p>
          ) : (
            <div className="space-y-2">
              {scheduledShifts.map((shift) => (
                <div key={shift.id} className="flex items-center justify-between border border-border rounded-lg px-4 py-3">
                  <p className="data text-sm">
                    {formatDateTime(shift.shiftStart)} → {formatDateTime(shift.shiftEnd)}
                  </p>
                  <button
                    className="btn-secondary text-xs py-1.5"
                    disabled={busy}
                    onClick={() => handleClockIn(shift.id)}
                  >
                    Clock in
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      <p className="text-sm font-semibold mb-3">Recent attendance</p>
      {attendance === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {attendance && attendance.length === 0 && <EmptyState message="No attendance records yet." />}
      {attendance && attendance.length > 0 && (
        <div className="card divide-y divide-border">
          {attendance.map((a) => (
            <div key={a.id} className="py-3 first:pt-0 last:pb-0 flex items-center justify-between">
              <p className="data text-sm">
                {formatDateTime(a.clockIn)} → {a.clockOut ? formatDateTime(a.clockOut) : "in progress"}
              </p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
