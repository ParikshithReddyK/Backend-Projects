import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";
import StatusBadge from "../../components/StatusBadge";

const TYPES = ["RESUME", "ID_PROOF", "TRANSCRIPT", "OTHER"];

export default function MyDocuments() {
  const [documents, setDocuments] = useState(null);
  const [file, setFile] = useState(null);
  const [type, setType] = useState("RESUME");
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState("");

  function refresh() {
    api.myDocuments().then(setDocuments).catch((e) => setError(e.message));
  }

  useEffect(refresh, []);

  async function handleUpload(e) {
    e.preventDefault();
    if (!file) return;
    setUploading(true);
    setError("");
    try {
      await api.uploadDocument(file, type);
      setFile(null);
      e.target.reset();
      refresh();
    } catch (err) {
      setError(err.message);
    } finally {
      setUploading(false);
    }
  }

  async function handleDownload(id) {
    try {
      await api.downloadDocument(id);
    } catch (e) {
      setError(e.message);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">My Documents</h1>
      <p className="text-sm text-ink-muted mb-8">Upload your resume and other supporting documents.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}

      <form onSubmit={handleUpload} className="card mb-6 flex items-end gap-3">
        <div className="flex-1">
          <label className="label">File</label>
          <input
            type="file"
            required
            className="input"
            onChange={(e) => setFile(e.target.files[0])}
          />
        </div>
        <div>
          <label className="label">Type</label>
          <select className="input" value={type} onChange={(e) => setType(e.target.value)}>
            {TYPES.map((t) => (
              <option key={t} value={t}>{t.replace("_", " ")}</option>
            ))}
          </select>
        </div>
        <button type="submit" disabled={uploading} className="btn-primary">
          {uploading ? "Uploading…" : "Upload"}
        </button>
      </form>

      {documents === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {documents && documents.length === 0 && <EmptyState message="No documents uploaded yet." />}
      {documents && documents.length > 0 && (
        <div className="card divide-y divide-border">
          {documents.map((doc) => (
            <div key={doc.id} className="py-3 first:pt-0 last:pb-0 flex items-center justify-between">
              <div>
                <p className="text-sm font-medium">{doc.fileName}</p>
                <p className="text-xs text-ink-faint">{doc.documentType.replace("_", " ")}</p>
              </div>
              <div className="flex items-center gap-3">
                <StatusBadge status={doc.verified ? "APPROVED" : "PENDING"} />
                <button
                  className="btn-secondary text-xs py-1.5"
                  onClick={() => handleDownload(doc.id)}
                >
                  Download
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
