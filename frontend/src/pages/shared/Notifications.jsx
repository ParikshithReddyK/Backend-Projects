import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

function timeAgo(iso) {
  const diff = Date.now() - new Date(iso).getTime();
  const mins = Math.floor(diff / 60000);
  if (mins < 1) return "just now";
  if (mins < 60) return `${mins}m ago`;
  const hrs = Math.floor(mins / 60);
  if (hrs < 24) return `${hrs}h ago`;
  return `${Math.floor(hrs / 24)}d ago`;
}

export default function Notifications() {
  const [notifications, setNotifications] = useState(null);
  const [error, setError] = useState("");

  function refresh() {
    api.myNotifications().then(setNotifications).catch((e) => setError(e.message));
  }

  useEffect(refresh, []);

  async function handleMarkRead(id) {
    try {
      await api.markNotificationRead(id);
      refresh();
    } catch (e) {
      setError(e.message);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Notifications</h1>
      <p className="text-sm text-ink-muted mb-8">Updates on your applications, leave, and more.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {notifications === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {notifications && notifications.length === 0 && <EmptyState message="No notifications yet." />}

      <div className="card divide-y divide-border">
        {notifications?.map((n) => (
          <div
            key={n.id}
            className={`py-3 first:pt-0 last:pb-0 flex items-start justify-between gap-4 ${
              n.isRead ? "opacity-50" : ""
            }`}
          >
            <div>
              <p className="text-xs font-semibold text-primary uppercase mb-0.5">
                {n.type.replace(/_/g, " ")}
              </p>
              <p className="text-sm">{n.message}</p>
              <p className="text-xs text-ink-faint mt-1">{timeAgo(n.createdAt)}</p>
            </div>
            {!n.isRead && (
              <button
                className="text-xs text-primary font-medium hover:underline shrink-0"
                onClick={() => handleMarkRead(n.id)}
              >
                Mark read
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
