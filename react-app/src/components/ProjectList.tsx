import { FaFolderOpen } from "react-icons/fa";

interface ProjectListProps {
  projects: any[];
  isMine: boolean;
  onCreate?: () => void;
}

const ProjectList: React.FC<ProjectListProps> = ({
  projects,
  isMine,
  onCreate,
}) => {
  if (projects.length > 0) {
    return (
      <div className="w-full max-w-4xl">
        {projects.map((p) => (
          <div
            key={p.id}
            className="mb-4 p-4 border border-green-300 rounded shadow-sm bg-white"
          >
            <h3 className="font-bold text-lg">{p.title}</h3>
            <p className="text-gray-600">{p.description}</p>
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="text-center">
      <div className="flex justify-center mb-4 text-yellow-500">
        {FaFolderOpen({ size: 64 }) as React.ReactElement}
      </div>
      <h2 className="text-2xl font-bold mb-2">
        {isMine ? "아직 프로젝트가 없습니다." : "프로젝트가 없습니다."}
      </h2>
      <p className="text-gray-600 mb-4">
        {isMine
          ? "지금 새 프로젝트를 만들어보세요!"
          : "커뮤니티 프로젝트가 아직 없거나 불러올 수 없습니다."}
      </p>
      {isMine && (
        <button
          className="bg-green-600 text-white px-6 py-2 rounded-xl hover:bg-green-700 transition shadow-md focus:outline-none focus:ring-2 focus:ring-green-400"
          onClick={onCreate}
        >
          + 새 프로젝트 만들기
        </button>
      )}
    </div>
  );
};

export default ProjectList;
