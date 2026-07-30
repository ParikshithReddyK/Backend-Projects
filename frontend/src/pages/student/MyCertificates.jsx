import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";

export default function MyCertificates() {
  const [certificates, setCertificates] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.myCertificates().then(setCertificates).catch((e) => setError(e.message));
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">My Certificates</h1>
      <p className="text-sm text-ink-muted mb-8">Certificates issued for your work.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {certificates === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {certificates && certificates.length === 0 && <EmptyState message="No certificates issued yet." />}

      <div className="space-y-3">
        {certificates?.map((c) => (
          <div key={c.id} className="card flex items-center justify-between">
            <div>
              <p className="text-sm font-semibold">{c.title}</p>
              <p className="data text-xs text-ink-faint">{c.certificateNumber} · Issued {c.issueDate}</p>
            </div>
            <p className="data text-sm font-medium">{c.totalHours} hrs</p>
          </div>
        ))}
      </div>
    </div>
  );
}
