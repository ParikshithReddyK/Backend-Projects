import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Seal from "./Seal";

const studentGroups = [
  {
    label: "Work",
    links: [
      { to: "/", label: "Dashboard" },
      { to: "/jobs", label: "Browse Jobs" },
      { to: "/applications", label: "My Applications" },
      { to: "/shifts", label: "My Shifts" },
      { to: "/clock", label: "Clock In / Out" },
      { to: "/payroll", label: "My Payroll" },
    ],
  },
  {
    label: "Growth",
    links: [
      { to: "/documents", label: "My Documents" },
      { to: "/training", label: "Training" },
      { to: "/reviews", label: "My Reviews" },
      { to: "/certificates", label: "My Certificates" },
    ],
  },
  {
    label: "Campus",
    links: [
      { to: "/leaves", label: "My Leave Requests" },
      { to: "/events", label: "Events" },
      { to: "/my-events", label: "My Registrations" },
      { to: "/equipment", label: "My Equipment" },
      { to: "/notifications", label: "Notifications" },
    ],
  },
];

const staffGroups = [
  {
    label: "Work",
    links: [
      { to: "/", label: "Dashboard" },
      { to: "/post-job", label: "Post a Job" },
      { to: "/manage-jobs", label: "Manage Jobs" },
    ],
  },
  {
    label: "People",
    links: [
      { to: "/leave-requests", label: "Leave Requests" },
      { to: "/give-review", label: "Give a Review" },
      { to: "/issue-certificates", label: "Issue Certificates" },
    ],
  },
  {
    label: "Campus",
    links: [
      { to: "/manage-events", label: "Manage Events" },
      { to: "/manage-equipment", label: "Manage Equipment" },
      { to: "/manage-training", label: "Training Modules" },
      { to: "/notifications", label: "Notifications" },
    ],
  },
];

function initials(name) {
  if (!name) return "?";
  return name.split(" ").map((p) => p[0]).slice(0, 2).join("").toUpperCase();
}

export default function Layout({ children }) {
  const { user, role, isStaff, logout } = useAuth();
  const navigate = useNavigate();
  const groups = isStaff ? staffGroups : studentGroups;

  function handleLogout() {
    logout();
    navigate("/login");
  }

  return (
    <div className="min-h-screen flex">
      <aside className="w-64 border-r border-border bg-surface flex flex-col">
        <div className="px-5 py-5 border-b border-border flex items-center gap-3">
          <Seal size={32} />
          <div>
            <p className="text-sm font-semibold leading-tight">Campus Workforce</p>
            <p className="text-xs text-ink-faint">Management</p>
          </div>
        </div>

        <nav className="flex-1 px-3 py-4 space-y-5 overflow-y-auto">
          {groups.map((group) => (
            <div key={group.label}>
              <p className="px-3 mb-1 text-[11px] font-semibold uppercase tracking-wide text-ink-faint">
                {group.label}
              </p>
              <div className="space-y-0.5">
                {group.links.map((link) => (
                  <NavLink
                    key={link.to}
                    to={link.to}
                    end={link.to === "/"}
                    className={({ isActive }) =>
                      `block px-3 py-1.5 rounded-lg text-sm font-medium transition-colors ${
                        isActive
                          ? "bg-primary-light text-primary"
                          : "text-ink-muted hover:bg-canvas hover:text-ink"
                      }`
                    }
                  >
                    {link.label}
                  </NavLink>
                ))}
              </div>
            </div>
          ))}
        </nav>

        <div className="px-4 py-4 border-t border-border flex items-center gap-3">
          <div className="w-8 h-8 rounded-full bg-ink text-white text-xs font-semibold flex items-center justify-center shrink-0">
            {initials(user?.fullName)}
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-medium truncate">{user?.fullName}</p>
            <p className="text-xs text-ink-faint">{role}</p>
          </div>
          <button
            onClick={handleLogout}
            className="text-xs text-ink-muted hover:text-danger transition-colors"
            title="Sign out"
          >
            Exit
          </button>
        </div>
      </aside>

      <main className="flex-1 bg-canvas overflow-y-auto">
        <div className="max-w-5xl mx-auto px-8 py-10">{children}</div>
      </main>
    </div>
  );
}
