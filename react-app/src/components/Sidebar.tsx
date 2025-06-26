import logo from "../assets/logo.png";

interface SidebarProps {
  viewMode: "mine" | "community";
}

const Sidebar: React.FC<SidebarProps> = ({ viewMode }) => (
  <aside className="w-64 bg-green-100 text-gray-900 shadow-md flex flex-col border-r border-green-200">
    <div className="flex items-center justify-center h-20 border-green-200">
      <img src={logo} alt="Logo" className="w-24 mt-6" />
    </div>
    <nav className="flex-1 px-4 py-6 space-y-1 text-sm font-medium">
      {viewMode === "community" ? (
        <>
          <SidebarItem label="프로젝트" active />
          <SidebarItem label="요청" />
          <SidebarItem label="대화" />
        </>
      ) : (
        <>
          <SidebarItem label="대시보드" active />
          <SidebarItem label="프로젝트" />
          <SidebarItem label="태스크" />
          <SidebarItem label="마일스톤" />
          <SidebarItem label="설정" />
        </>
      )}
    </nav>
  </aside>
);

const SidebarItem = ({
  label,
  active = false,
}: {
  label: string;
  active?: boolean;
}) => (
  <a
    href="#"
    className={`block px-3 py-2 rounded-md ${
      active
        ? "bg-white text-green-600 shadow-sm"
        : "hover:bg-green-200 hover:text-green-800 transition"
    }`}
  >
    {label}
  </a>
);

export default Sidebar;
