export default function StatCard({ label, value, hint }) {
  return (
    <div className="stat-card">
      <p className="text-xs font-medium text-ink-muted">{label}</p>
      <p className="data text-2xl font-semibold text-ink">{value}</p>
      {hint && <p className="text-xs text-ink-faint">{hint}</p>}
    </div>
  );
}
