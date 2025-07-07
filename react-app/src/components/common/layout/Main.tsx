import { useState } from "react";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import Sidebar from "./Sidebar";
import Header from "./Header";
import { Menu, ViewMode } from "../../../types/common";
import { getSelectedMenuFromPath, menuToPath } from "../../../utils/ts/menu";
import { useAuth } from "../../../App";

const Main = () => {
  const [viewMode, setViewMode] = useState<ViewMode>("private");

  const navigate = useNavigate();
  const location = useLocation();
  const { blokey } = useAuth();

  const selectedMenu = getSelectedMenuFromPath(location.pathname);

  return (
    <div className="font-stardust flex h-screen bg-gray-50">
      <div className="w-48 shrink-0">
        <Sidebar
          viewMode={viewMode}
          selectedMenu={selectedMenu}
          onMenuClick={(menu: Menu) => {
            const path = menuToPath[menu] || "/private/dashboard";
            navigate(path);
          }}
        />
      </div>
      <div className="flex-1 flex flex-col">
        <Header
          viewMode={viewMode}
          setViewMode={setViewMode}
          selectedMenu={selectedMenu}
          setSelectedMenu={() => {}}
          blokey={blokey}
        />
        <main
          className="flex-1 flex flex-col items-center justify-start p-4 bg-gray-100"
          style={{ minHeight: "calc(100vh - 80px)" }}
        >
          <div className="flex w-full max-w-7xl bg-white rounded-2xl shadow-lg p-4 flex-grow">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
};

export default Main;
