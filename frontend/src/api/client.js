const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

function getToken() {
  return localStorage.getItem("cwm_token");
}

async function request(path, { method = "GET", body, auth = true } = {}) {
  const headers = { "Content-Type": "application/json" };
  if (auth) {
    const token = getToken();
    if (token) headers["Authorization"] = `Bearer ${token}`;
  }

  const res = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  if (!res.ok) {
    let message = `Request failed (${res.status})`;
    try {
      const data = await res.json();
      message = data.message || message;
    } catch {
      // no JSON body
    }
    throw new Error(message);
  }

  if (res.status === 204) return null;
  return res.json();
}

async function upload(path, formData) {
  const headers = {};
  const token = getToken();
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const res = await fetch(`${BASE_URL}${path}`, {
    method: "POST",
    headers,
    body: formData,
  });

  if (!res.ok) {
    let message = `Upload failed (${res.status})`;
    try {
      const data = await res.json();
      message = data.message || message;
    } catch {
      // no JSON body
    }
    throw new Error(message);
  }
  return res.json();
}

async function download(path) {
  const headers = {};
  const token = getToken();
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const res = await fetch(`${BASE_URL}${path}`, { headers });
  if (!res.ok) throw new Error(`Download failed (${res.status})`);

  const blob = await res.blob();
  const disposition = res.headers.get("Content-Disposition") || "";
  const match = disposition.match(/filename="?([^"]+)"?/);
  const filename = match ? match[1] : "download";

  const url = window.URL.createObjectURL(blob);
  const a = document.createElement("a");
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  a.remove();
  window.URL.revokeObjectURL(url);
}

export const api = {
  login: (email, password) =>
    request("/api/users/login", { method: "POST", body: { email, password }, auth: false }),
  register: (payload) =>
    request("/api/users/register", { method: "POST", body: payload, auth: false }),
  me: () => request("/api/users/me"),

  browseJobs: () => request("/api/jobs/browse"),
  createJob: (payload) => request("/api/jobs", { method: "POST", body: payload }),
  myJobs: () => request("/api/jobs/mine"),

  applyToJob: (jobId) => request("/api/applications", { method: "POST", body: { jobId } }),
  myApplications: () => request("/api/applications/mine"),
  applicationsForJob: (jobId) => request(`/api/applications/job/${jobId}`),
  updateApplicationStatus: (id, status) =>
    request(`/api/applications/${id}/status?status=${status}`, { method: "PATCH" }),

  myShifts: () => request("/api/shifts/mine"),

  myPayroll: () => request("/api/payroll/mine"),

  jobsOverview: () => request("/api/analytics/jobs-overview"),

  // Attendance
  clockIn: (shiftId) => request("/api/attendance/clock-in", { method: "POST", body: { shiftId } }),
  clockOut: () => request("/api/attendance/clock-out", { method: "PATCH" }),
  myAttendance: () => request("/api/attendance/mine"),

  // Documents
  uploadDocument: (file, type) => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("type", type);
    return upload("/api/documents", formData);
  },
  myDocuments: () => request("/api/documents/mine"),
  downloadDocument: (id, filename) => download(`/api/documents/${id}/download`, filename),
  verifyDocument: (id) => request(`/api/documents/${id}/verify`, { method: "PATCH" }),

  // Leave
  requestLeave: (payload) => request("/api/leaves", { method: "POST", body: payload }),
  myLeaves: () => request("/api/leaves/mine"),
  leavesForJob: (jobId) => request(`/api/leaves/job/${jobId}`),
  updateLeaveStatus: (id, status) =>
    request(`/api/leaves/${id}/status?status=${status}`, { method: "PATCH" }),

  // Performance
  createReview: (payload) => request("/api/performance/reviews", { method: "POST", body: payload }),
  myReviews: () => request("/api/performance/reviews/mine"),
  reviewsForJob: (jobId) => request(`/api/performance/reviews/job/${jobId}`),

  // Certificates
  issueCertificate: (payload) => request("/api/certificates", { method: "POST", body: payload }),
  myCertificates: () => request("/api/certificates/mine"),

  // Notifications
  myNotifications: () => request("/api/notifications/mine"),
  markNotificationRead: (id) => request(`/api/notifications/${id}/read`, { method: "PATCH" }),

  // Events
  createEvent: (payload) => request("/api/events", { method: "POST", body: payload }),
  browseEvents: () => request("/api/events/browse"),
  registerForEvent: (eventId) => request(`/api/events/${eventId}/register`, { method: "POST" }),
  myEventRegistrations: () => request("/api/events/registrations/mine"),
  markAttended: (registrationId) =>
    request(`/api/events/registrations/${registrationId}/attended`, { method: "PATCH" }),

  // Equipment
  addEquipment: (payload) => request("/api/equipment", { method: "POST", body: payload }),
  browseEquipment: () => request("/api/equipment/browse"),
  assignEquipment: (payload) => request("/api/equipment/assign", { method: "POST", body: payload }),
  returnEquipment: (assignmentId) =>
    request(`/api/equipment/assignments/${assignmentId}/return`, { method: "PATCH" }),
  myEquipmentAssignments: () => request("/api/equipment/assignments/mine"),

  // Training
  createModule: (payload) => request("/api/training/modules", { method: "POST", body: payload }),
  browseModules: () => request("/api/training/modules/browse"),
  enrollInModule: (moduleId) => request(`/api/training/modules/${moduleId}/enroll`, { method: "POST" }),
  completeTraining: (completionId, score) =>
    request(`/api/training/completions/${completionId}/complete`, {
      method: "PATCH",
      body: { score },
    }),
  myCompletions: () => request("/api/training/completions/mine"),
};

export { getToken };
