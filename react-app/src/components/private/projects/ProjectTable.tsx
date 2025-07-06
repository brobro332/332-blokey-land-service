import React from "react";
import { Project } from "../../../types/project";

interface ProjectTableProps {
  projects: Project[];
}

const ProjectTable: React.FC<ProjectTableProps> = ({ projects }) => {
  return (
    <div className="overflow-x-auto mt-2">
      <table className="min-w-full border text-sm text-left">
        <thead className="bg-gray-100">
          <tr>
            <th className="px-4 py-2">제목</th>
            <th className="px-4 py-2">설명</th>
            <th className="px-4 py-2">상태</th>
            <th className="px-4 py-2">공개 여부</th>
            <th className="px-4 py-2">예정시작일</th>
            <th className="px-4 py-2">예정종료일</th>
            <th className="px-4 py-2">실제시작일</th>
            <th className="px-4 py-2">실제종료일</th>
          </tr>
        </thead>
        <tbody>
          {projects.map((p, i) => (
            <tr key={i} className="border-t">
              <td className="px-4 py-2 whitespace-nowrap">{p.title}</td>
              <td className="px-4 py-2 whitespace-nowrap">{p.description}</td>
              <td className="px-4 py-2 whitespace-nowrap">{p.status}</td>
              <td className="px-4 py-2 whitespace-nowrap">
                {p.isPrivate ? "비공개" : "공개"}
              </td>
              <td className="px-4 py-2 whitespace-nowrap">
                {p.estimatedStartDate}
              </td>
              <td className="px-4 py-2 whitespace-nowrap">
                {p.estimatedEndDate}
              </td>
              <td className="px-4 py-2 whitespace-nowrap">
                {p.actualStartDate}
              </td>
              <td className="px-4 py-2 whitespace-nowrap">{p.actualEndDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ProjectTable;
