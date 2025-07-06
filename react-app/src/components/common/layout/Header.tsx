import React, { useState } from "react";
import { FaSearch, FaBars } from "react-icons/fa";
import { Menu, ViewMode } from "../../../types/common";
import { Blokey } from "../../../types/blokey";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../../App";

interface HeaderProps {
  viewMode: ViewMode;
  setViewMode: (mode: ViewMode) => void;
  selectedMenu: Menu;
  setSelectedMenu: (menu: Menu) => void;
  blokey: Blokey | null;
}

const Header: React.FC<HeaderProps> = ({
  viewMode,
  setViewMode,
  setSelectedMenu,
  blokey,
}) => {
  const navigate = useNavigate();
  const { logout } = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <header className="h-20 bg-green-100 border-b border-green-200 px-6 flex items-center justify-between shadow-sm">
      <div className="flex flex-1 max-w-xl items-center">
        <input
          type="text"
          placeholder="검색어를 입력하세요."
          className="flex-grow px-4 py-[10px] border border-green-300 rounded-l-md text-base leading-tight focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-400"
        />
        <button
          className="bg-green-600 hover:bg-green-700 text-white px-4 py-[10px] rounded-r-md flex items-center justify-center"
          aria-label="검색"
        >
          {FaSearch({ size: 22 })}
        </button>

        <div className="ml-6">
          <div className="flex items-center bg-white border border-green-300 rounded-full shadow-sm text-sm font-semibold overflow-hidden">
            <ToggleButton
              label="내 프로젝트"
              active={viewMode === "private"}
              onClick={() => {
                setViewMode("private");
                setSelectedMenu("private-dashboard");
              }}
            />
            <ToggleButton
              label="커뮤니티"
              active={viewMode === "community"}
              onClick={() => {
                setViewMode("community");
                setSelectedMenu("community-projects");
              }}
            />
          </div>
        </div>
      </div>

      <div className="relative flex items-center space-x-3">
        <span
          className="text-gray-700 text-sm max-w-16 truncate"
          title={blokey?.nickname}
        >
          {blokey?.nickname ?? "알 수 없음"}
        </span>
        <span>님 환영합니다.</span>

        <div className="relative">
          <button
            onClick={() => setMenuOpen(!menuOpen)}
            className="w-8 h-8 bg-white text-green-700 border-4 border-green-600 rounded-md flex items-center justify-center
             shadow-sm hover:shadow-md hover:bg-green-50
             transition duration-200 ease-in-out
             focus:outline-none focus:ring-2 focus:ring-green-400 focus:ring-offset-1"
            aria-label="메뉴"
          >
            {FaBars({ size: 15 })}
          </button>

          {menuOpen && (
            <div className="absolute right-0 mt-2 w-36 bg-white border border-green-200 rounded-md shadow-lg z-50">
              <button
                onClick={() => {
                  navigate("/common/mypage");
                  setMenuOpen(false);
                }}
                className="block w-full text-left px-4 py-2 text-sm text-green-700 hover:bg-green-100"
              >
                마이페이지
              </button>
              <button
                onClick={() => {
                  logout();
                  setMenuOpen(false);
                }}
                className="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-100"
              >
                로그아웃
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

const ToggleButton = ({
  label,
  active,
  onClick,
}: {
  label: string;
  active: boolean;
  onClick: () => void;
}) => (
  <button
    className={`px-4 py-1 transition-all duration-200 ${
      active ? "bg-green-600 text-white" : "text-green-600 hover:bg-green-100"
    }`}
    onClick={onClick}
  >
    {label}
  </button>
);

export default Header;
