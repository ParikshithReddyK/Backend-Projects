import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Login from "./pages/Login";

import StudentDashboard from "./pages/dashboard/StudentDashboard";
import AdminDashboard from "./pages/dashboard/AdminDashboard";

import BrowseJobs from "./pages/student/BrowseJobs";
import MyApplications from "./pages/student/MyApplications";
import MyShifts from "./pages/student/MyShifts";
import MyPayroll from "./pages/student/MyPayroll";
import ClockInOut from "./pages/student/ClockInOut";
import MyDocuments from "./pages/student/MyDocuments";
import MyLeaves from "./pages/student/MyLeaves";
import MyReviews from "./pages/student/MyReviews";
import MyCertificates from "./pages/student/MyCertificates";
import BrowseEvents from "./pages/student/BrowseEvents";
import MyEvents from "./pages/student/MyEvents";
import MyEquipment from "./pages/student/MyEquipment";
import BrowseTraining from "./pages/student/BrowseTraining";

import PostJob from "./pages/admin/PostJob";
import ManageJobs from "./pages/admin/ManageJobs";
import LeaveRequests from "./pages/admin/LeaveRequests";
import GiveReview from "./pages/admin/GiveReview";
import IssueCertificates from "./pages/admin/IssueCertificates";
import ManageEvents from "./pages/admin/ManageEvents";
import ManageEquipment from "./pages/admin/ManageEquipment";
import ManageTraining from "./pages/admin/ManageTraining";

import Notifications from "./pages/shared/Notifications";

function Home() {
  const { isStaff } = useAuth();
  return isStaff ? <AdminDashboard /> : <StudentDashboard />;
}

function wrap(el, opts = {}) {
  return <ProtectedRoute {...opts}>{el}</ProtectedRoute>;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />

          <Route path="/" element={wrap(<Home />)} />

          {/* Student */}
          <Route path="/jobs" element={wrap(<BrowseJobs />)} />
          <Route path="/applications" element={wrap(<MyApplications />)} />
          <Route path="/shifts" element={wrap(<MyShifts />)} />
          <Route path="/clock" element={wrap(<ClockInOut />)} />
          <Route path="/payroll" element={wrap(<MyPayroll />)} />
          <Route path="/documents" element={wrap(<MyDocuments />)} />
          <Route path="/training" element={wrap(<BrowseTraining />)} />
          <Route path="/reviews" element={wrap(<MyReviews />)} />
          <Route path="/certificates" element={wrap(<MyCertificates />)} />
          <Route path="/leaves" element={wrap(<MyLeaves />)} />
          <Route path="/events" element={wrap(<BrowseEvents />)} />
          <Route path="/my-events" element={wrap(<MyEvents />)} />
          <Route path="/equipment" element={wrap(<MyEquipment />)} />

          {/* Shared */}
          <Route path="/notifications" element={wrap(<Notifications />)} />

          {/* Staff */}
          <Route path="/post-job" element={wrap(<PostJob />, { staffOnly: true })} />
          <Route path="/manage-jobs" element={wrap(<ManageJobs />, { staffOnly: true })} />
          <Route path="/leave-requests" element={wrap(<LeaveRequests />, { staffOnly: true })} />
          <Route path="/give-review" element={wrap(<GiveReview />, { staffOnly: true })} />
          <Route path="/issue-certificates" element={wrap(<IssueCertificates />, { staffOnly: true })} />
          <Route path="/manage-events" element={wrap(<ManageEvents />, { staffOnly: true })} />
          <Route path="/manage-equipment" element={wrap(<ManageEquipment />, { staffOnly: true })} />
          <Route path="/manage-training" element={wrap(<ManageTraining />, { staffOnly: true })} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
