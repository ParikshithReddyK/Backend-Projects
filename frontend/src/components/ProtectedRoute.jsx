import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Layout from "./Layout";

export default function ProtectedRoute({ children, staffOnly = false }) {
  const { user, isStaff, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-canvas">
        <p className="text-sm text-ink/50">Loading…</p>
      </div>
    );
  }

  if (!user) return <Navigate to="/login" replace />;
  if (staffOnly && !isStaff) return <Navigate to="/jobs" replace />;

  return <Layout>{children}</Layout>;
}
