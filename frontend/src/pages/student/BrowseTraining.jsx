import { useEffect, useState } from "react";
import { api } from "../../api/client";
import EmptyState from "../../components/EmptyState";
import StatusBadge from "../../components/StatusBadge";

export default function BrowseTraining() {
  const [modules, setModules] = useState(null);
  const [completions, setCompletions] = useState(null);
  const [busyId, setBusyId] = useState(null);
  const [scoreInputs, setScoreInputs] = useState({});
  const [error, setError] = useState("");

  function refresh() {
    api.browseModules().then(setModules).catch((e) => setError(e.message));
    api.myCompletions().then(setCompletions).catch((e) => setError(e.message));
  }

  useEffect(refresh, []);

  function completionFor(moduleId) {
    return completions?.find((c) => c.moduleId === moduleId);
  }

  async function handleEnroll(moduleId) {
    setBusyId(moduleId);
    setError("");
    try {
      await api.enrollInModule(moduleId);
      refresh();
    } catch (e) {
      setError(e.message);
    } finally {
      setBusyId(null);
    }
  }

  async function handleComplete(completionId) {
    setBusyId(completionId);
    setError("");
    try {
      const score = Number(scoreInputs[completionId] ?? 0);
      await api.completeTraining(completionId, score);
      refresh();
    } catch (e) {
      setError(e.message);
    } finally {
      setBusyId(null);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Training Modules</h1>
      <p className="text-sm text-ink-muted mb-8">Complete required training and compliance modules.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {modules === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {modules && modules.length === 0 && <EmptyState message="No training modules available." />}

      <div className="space-y-4">
        {modules?.map((mod) => {
          const completion = completionFor(mod.id);
          return (
            <div key={mod.id} className="card">
              <div className="flex items-start justify-between gap-4">
                <div>
                  <p className="text-sm font-semibold">{mod.title}</p>
                  <p className="text-xs text-ink-faint">{mod.category}</p>
                </div>
                {completion && <StatusBadge status={completion.status} />}
              </div>
              <p className="text-sm text-ink-muted mt-3">{mod.description}</p>

              <div className="mt-4 pt-4 border-t border-border">
                {!completion && (
                  <button
                    className="btn-primary text-xs py-1.5"
                    disabled={busyId === mod.id}
                    onClick={() => handleEnroll(mod.id)}
                  >
                    {busyId === mod.id ? "Enrolling…" : "Enroll"}
                  </button>
                )}
                {completion && completion.status === "PENDING" && (
                  <div className="flex items-center gap-2">
                    <input
                      type="number"
                      min="0"
                      max="100"
                      placeholder="Score"
                      className="input w-24"
                      value={scoreInputs[completion.id] ?? ""}
                      onChange={(e) =>
                        setScoreInputs((prev) => ({ ...prev, [completion.id]: e.target.value }))
                      }
                    />
                    <button
                      className="btn-primary text-xs py-1.5"
                      disabled={busyId === completion.id}
                      onClick={() => handleComplete(completion.id)}
                    >
                      {busyId === completion.id ? "Submitting…" : "Mark complete"}
                    </button>
                  </div>
                )}
                {completion?.status === "COMPLETED" && (
                  <p className="data text-sm text-ink-muted">Score: {completion.score}</p>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
