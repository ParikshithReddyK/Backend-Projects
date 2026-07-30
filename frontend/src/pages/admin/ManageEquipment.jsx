import { useEffect, useState } from "react";
import { api } from "../../api/client";
import StatusBadge from "../../components/StatusBadge";
import EmptyState from "../../components/EmptyState";

export default function ManageEquipment() {
  const [equipment, setEquipment] = useState(null);
  const [form, setForm] = useState({ name: "", category: "", serialNumber: "" });
  const [assignForm, setAssignForm] = useState({ equipmentId: "", studentId: "" });
  const [returnId, setReturnId] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  function refresh() {
    api.browseEquipment().then(setEquipment).catch((e) => setError(e.message));
  }

  useEffect(refresh, []);

  async function handleAdd(e) {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    try {
      await api.addEquipment(form);
      setForm({ name: "", category: "", serialNumber: "" });
      refresh();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  async function handleAssign(e) {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    setMessage("");
    try {
      await api.assignEquipment({
        equipmentId: Number(assignForm.equipmentId),
        studentId: Number(assignForm.studentId),
      });
      setAssignForm({ equipmentId: "", studentId: "" });
      setMessage("Equipment assigned.");
      refresh();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  async function handleReturn(e) {
    e.preventDefault();
    setSubmitting(true);
    setError("");
    setMessage("");
    try {
      await api.returnEquipment(Number(returnId));
      setReturnId("");
      setMessage("Equipment marked as returned.");
      refresh();
    } catch (err) {
      setError(err.message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-1">Manage Equipment</h1>
      <p className="text-sm text-ink-muted mb-8">Add inventory and assign or return items.</p>

      {error && <p className="text-sm text-danger mb-4">{error}</p>}
      {message && <p className="text-sm text-success mb-4">{message}</p>}

      <div className="grid grid-cols-2 gap-4 mb-6">
        <form onSubmit={handleAdd} className="card space-y-3">
          <p className="text-sm font-semibold">Add equipment</p>
          <input required className="input" placeholder="Name" value={form.name} onChange={(e) => setForm((p) => ({ ...p, name: e.target.value }))} />
          <input required className="input" placeholder="Category" value={form.category} onChange={(e) => setForm((p) => ({ ...p, category: e.target.value }))} />
          <input required className="input" placeholder="Serial number" value={form.serialNumber} onChange={(e) => setForm((p) => ({ ...p, serialNumber: e.target.value }))} />
          <button type="submit" disabled={submitting} className="btn-primary w-full">Add</button>
        </form>

        <div className="space-y-4">
          <form onSubmit={handleAssign} className="card space-y-3">
            <p className="text-sm font-semibold">Assign equipment</p>
            <input required type="number" className="input" placeholder="Equipment ID" value={assignForm.equipmentId} onChange={(e) => setAssignForm((p) => ({ ...p, equipmentId: e.target.value }))} />
            <input required type="number" className="input" placeholder="Student ID" value={assignForm.studentId} onChange={(e) => setAssignForm((p) => ({ ...p, studentId: e.target.value }))} />
            <button type="submit" disabled={submitting} className="btn-secondary w-full">Assign</button>
          </form>

          <form onSubmit={handleReturn} className="card space-y-3">
            <p className="text-sm font-semibold">Return equipment</p>
            <input required type="number" className="input" placeholder="Assignment ID" value={returnId} onChange={(e) => setReturnId(e.target.value)} />
            <button type="submit" disabled={submitting} className="btn-secondary w-full">Mark returned</button>
          </form>
        </div>
      </div>

      <p className="text-sm font-semibold mb-3">Inventory</p>
      {equipment === null && <p className="text-sm text-ink-muted">Loading…</p>}
      {equipment && equipment.length === 0 && <EmptyState message="No equipment added yet." />}
      <div className="card divide-y divide-border">
        {equipment?.map((item) => (
          <div key={item.id} className="py-3 first:pt-0 last:pb-0 flex items-center justify-between">
            <div>
              <p className="text-sm font-medium">{item.name} <span className="text-ink-faint">#{item.id}</span></p>
              <p className="data text-xs text-ink-faint">{item.serialNumber} · {item.category}</p>
            </div>
            <StatusBadge status={item.status} />
          </div>
        ))}
      </div>
    </div>
  );
}
