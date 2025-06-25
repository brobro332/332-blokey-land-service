import React, { useEffect, useState, createContext, useContext } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { JSX } from "react/jsx-runtime";
import LoginPage from "./pages/LoginPage";
import { apiAxios } from "./utils/Api";
import "./App.css";
import MainPage from "./pages/MainPage";
import JoinPage from "./pages/JoinPage";

const AuthContext = createContext<{
  isAuthenticated: boolean;
  setIsAuthenticated: (val: boolean) => void;
}>({
  isAuthenticated: false,
  setIsAuthenticated: () => {},
});
export const useAuth = () => useContext(AuthContext);

const ProtectedRoute: React.FC<{ children: JSX.Element }> = ({ children }) => {
  const { isAuthenticated } = useAuth();
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  return children;
};

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    apiAxios("/accounts/session")
      .then(() => setIsAuthenticated(true))
      .catch(() => setIsAuthenticated(false))
      .finally(() => setChecking(false));
  }, []);

  if (checking) return <div>Loading...</div>;

  return (
    <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated }}>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/join" element={<JoinPage />} />
          <Route
            path="/main"
            element={
              <ProtectedRoute>
                <MainPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="*"
            element={
              <Navigate to={isAuthenticated ? "/main" : "/login"} replace />
            }
          />
        </Routes>
      </BrowserRouter>
    </AuthContext.Provider>
  );
};

export default App;
