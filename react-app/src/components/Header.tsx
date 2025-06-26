import { FaSearch } from "react-icons/fa";

interface HeaderProps {
  viewMode: "mine" | "community";
  setViewMode: (mode: "mine" | "community") => void;
}

const Header: React.FC<HeaderProps> = ({ viewMode, setViewMode }) => (
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
            active={viewMode === "mine"}
            onClick={() => setViewMode("mine")}
          />
          <ToggleButton
            label="커뮤니티"
            active={viewMode === "community"}
            onClick={() => setViewMode("community")}
          />
        </div>
      </div>
    </div>
    <div className="flex items-center space-x-3">
      <div className="w-8 h-8 rounded-full bg-gray-400 text-white text-sm flex items-center justify-center font-semibold">
        U
      </div>
      <span className="text-gray-700 text-sm">사용자</span>
    </div>
  </header>
);

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
