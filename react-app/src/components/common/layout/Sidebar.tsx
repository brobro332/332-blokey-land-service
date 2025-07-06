import React from "react";
import { useNavigate } from "react-router-dom";
import logo from "../../../assets/image/logo.png";
import { Menu, ViewMode } from "../../../types/common";

interface SidebarProps {
  viewMode: ViewMode;
  selectedMenu: Menu;
  onMenuClick: (menu: Menu) => void;
}

const Sidebar: React.FC<SidebarProps> = ({
  viewMode,
  selectedMenu,
  onMenuClick,
}) => {
  const navigate = useNavigate();

  return (
    <aside className="w-48 h-full bg-green-100 text-gray-900 shadow-md flex flex-col border-r border-green-200">
      <div className="flex items-center justify-center border-green-200">
        <img
          src={logo}
          alt="Logo"
          className="w-24 mt-6 cursor-pointer"
          onClick={() => navigate("/private/dashboard")}
        />
      </div>

      <nav className="flex-1 px-4 py-6 space-y-1 text-sm font-medium">
        {viewMode === "community" ? (
          <SidebarItem
            label="프로젝트"
            active={selectedMenu === "community-projects"}
            onClick={() => onMenuClick("community-projects")}
          />
        ) : (
          <>
            <SidebarItem
              label="대시보드"
              active={selectedMenu === "private-dashboard"}
              onClick={() => onMenuClick("private-dashboard")}
            />
            <SidebarItem
              label="프로젝트"
              active={selectedMenu === "private-projects"}
              onClick={() => onMenuClick("private-projects")}
            />
            <SidebarItem
              label="마일스톤"
              active={selectedMenu === "private-milestones"}
              onClick={() => onMenuClick("private-milestones")}
            />
          </>
        )}
      </nav>
    </aside>
  );
};

interface SidebarItemProps {
  label: string;
  active?: boolean;
  onClick?: () => void;
}

const SidebarItem: React.FC<SidebarItemProps> = ({
  label,
  active = false,
  onClick,
}) => (
  <button
    onClick={onClick}
    className={`block w-full text-left px-3 py-2 rounded-md ${
      active
        ? "bg-white text-green-600 shadow-sm"
        : "hover:bg-green-200 hover:text-green-800 transition"
    }`}
  >
    {label}
  </button>
);

export default Sidebar;
