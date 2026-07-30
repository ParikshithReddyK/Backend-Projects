import { createContext, useContext, useEffect, useState } from "react";
import { api } from "../api/client";

const AuthContext = createContext(null);

function decodeRole(token) {
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.role || null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [role, setRole] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("cwm_token");
    if (!token) {
      setLoading(false);
      return;
    }
    setRole(decodeRole(token));
    api
      .me()
      .then(setUser)
      .catch(() => {
        localStorage.removeItem("cwm_token");
      })
      .finally(() => setLoading(false));
  }, []);

  async function login(email, password) {
    const { token } = await api.login(email, password);
    localStorage.setItem("cwm_token", token);
    setRole(decodeRole(token));
    const me = await api.me();
    setUser(me);
    return me;
  }

  function logout() {
    localStorage.removeItem("cwm_token");
    setUser(null);
    setRole(null);
  }

  const isStaff = role === "ADMIN" || role === "HR_MANAGER" || role === "SUPERVISOR";

  return (
    <AuthContext.Provider value={{ user, role, isStaff, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
