import React, { useEffect, useState, createContext, useContext } from "react";
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
  useNavigate,
} from "react-router-dom";
import { apiAxios } from "./utils/tsx/Api";
import "./App.css";
import Loading from "./components/common/layout/Loading";
import { LoadingProvider, useLoading } from "./contexts/LoadingContext";
import { loadingController } from "./utils/ts/loadingController";
import Main from "./components/common/layout/Main";
import LoginPage from "./pages/common/authentication/LoginPage";
import ProjectsPage from "./pages/private/ProjectsPage";
import TaskBoard from "./components/private/tasks/TaskBoard";
import MilestonesPage from "./pages/private/MilestonesPage";
import JoinPage from "./pages/common/authentication/JoinPage";
import DashboardPage from "./pages/private/DashboardPage";
import { Blokey } from "./types/blokey";
import MyPage from "./pages/common/mypage/MyPage";

interface AuthContextType {
  isAuthenticated: boolean;
  setIsAuthenticated: (val: boolean) => void;
  checking: boolean;
  blokey: Blokey | null;
  setBlokey: (b: Blokey | null) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  setIsAuthenticated: () => {},
  checking: true,
  blokey: null,
  setBlokey: () => {},
  logout: () => {},
});

export const useAuth = () => useContext(AuthContext);

const ProtectedRoute: React.FC<{ children: React.ReactElement }> = ({
  children,
}) => {
  const { isAuthenticated, checking } = useAuth();

  if (checking) return <Loading loading={true} />;

  if (!isAuthenticated) return <Navigate to="/login" replace />;

  return children;
};

const GlobalLoader = () => {
  const { loading, setLoading } = useLoading();

  useEffect(() => {
    loadingController.bind(setLoading);
  }, [setLoading]);

  return <Loading loading={loading} />;
};

const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [checking, setChecking] = useState(true);
  const [blokey, setBlokey] = useState<Blokey | null>(null);
  const navigate = useNavigate();

  const logout = () => {
    apiAxios("/api/accounts/session", {
      method: "DELETE",
      withCredentials: true,
    })
      .catch(() => {})
      .finally(() => {
        setIsAuthenticated(false);
        setBlokey(null);
        navigate("/login");
      });
  };

  useEffect(() => {
    apiAxios("/api/accounts/session", { withCredentials: true })
      .then(() => {
        setIsAuthenticated(true);
        return apiAxios<Blokey>("/blokey-land/api/blokeys/me", {
          method: "GET",
          withCredentials: true,
        });
      })
      .then((data) => {
        setBlokey(data);
      })
      .catch(() => {
        setIsAuthenticated(false);
        setBlokey(null);
      })
      .finally(() => setChecking(false));
  }, []);

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        setIsAuthenticated,
        checking,
        blokey,
        setBlokey,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

const App = () => {
  return (
    <LoadingProvider>
      <GlobalLoader />
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
                path="/"
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
    </LoadingProvider>
  );
};

export default App;
