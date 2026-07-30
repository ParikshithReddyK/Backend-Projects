const STYLES = {
  OPEN: "bg-success-light text-success",
  ACCEPTED: "bg-success-light text-success",
  APPROVED: "bg-success-light text-success",
  ATTENDED: "bg-success-light text-success",
  COMPLETED: "bg-success-light text-success",
  PENDING: "bg-warn-light text-warn",
  SCHEDULED: "bg-primary-light text-primary",
  CLOSED: "bg-ink-faint/10 text-ink-muted",
  FILLED: "bg-ink-faint/10 text-ink-muted",
  CANCELLED: "bg-danger-light text-danger",
  REJECTED: "bg-danger-light text-danger",
};

export default function StatusBadge({ status }) {
  const style = STYLES[status] || "bg-ink-faint/10 text-ink-muted";
  return (
    <span
      className={`inline-block px-2.5 py-1 rounded-md text-xs font-semibold ${style}`}
    >
      {status}
    </span>
  );
}
