import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./contexts/AuthContext";
import Loading from "./components/common/layout/Loading";
import Main from "./components/common/layout/Main";
import LoginPage from "./pages/common/authentication/LoginPage";
import ProjectsPage from "./pages/private/ProjectsPage";
import TaskBoard from "./components/private/tasks/TaskBoard";
import MilestonesPage from "./pages/private/MilestonesPage";
import JoinPage from "./pages/common/authentication/JoinPage";
import DashboardPage from "./pages/private/DashboardPage";
import MyPage from "./pages/common/mypage/MyPage";
import "./App.css";

const ProtectedRoute: React.FC<{ children: React.ReactElement }> = ({
  children,
}) => {
  const { isAuthenticated, checking } = useAuth();

  if (checking) return <Loading loading={true} />;
  if (!isAuthenticated) return <Navigate to="/login" replace />;

  return children;
};

const App = () => {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <Main />
              </ProtectedRoute>
            }
          >
            <Route
              index
              element={<Navigate to="/private/dashboard" replace />}
            />
            <Route path="private/dashboard" element={<DashboardPage />} />
            <Route path="private/projects" element={<ProjectsPage />} />
            <Route path="private/tasks" element={<TaskBoard />} />
            <Route path="private/milestones" element={<MilestonesPage />} />
            <Route path="common/mypage" element={<MyPage />} />
          </Route>

          <Route path="/login" element={<LoginPage />} />
          <Route path="/join" element={<JoinPage />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
};

export default App;
