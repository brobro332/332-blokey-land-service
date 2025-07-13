import React, { createContext, useContext, useState, useEffect } from "react";
import { apiAxios } from "../utils/tsx/Api";
import { Blokey } from "../types/blokey";

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

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [checking, setChecking] = useState(true);
  const [blokey, setBlokey] = useState<Blokey | null>(null);

  useEffect(() => {
    console.log("AuthProvider: checking started");
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
      .finally(() => {
        setChecking(false);
      });
  }, []);

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        setIsAuthenticated,
        checking,
        blokey,
        setBlokey,
        logout: () => {
          setIsAuthenticated(false);
          setBlokey(null);
        },
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
