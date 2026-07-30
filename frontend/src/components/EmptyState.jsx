export default function EmptyState({ message, action }) {
  return (
    <div className="card text-center py-12">
      <p className="text-ink/60 text-sm mb-3">{message}</p>
      {action}
    </div>
  );
}
