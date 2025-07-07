import React from "react";
import * as FaIcons from "react-icons/fa";
import { IconBaseProps } from "react-icons";
import defaultProjectImage from "../../../assets/image/default-project.png";
import { Project } from "../../../types/project";
import { useNavigate } from "react-router-dom";

const EditIcon = FaIcons.FaEdit as React.FC<IconBaseProps>;
const TrashIcon = FaIcons.FaTrash as React.FC<IconBaseProps>;

interface ProjectCardProps {
  project: Project;
  onSelect?: (project: Project) => void;
  onEditClick?: (project: Project) => void;
  onDelete?: (id: number) => void;
  isLeader?: boolean;
}

const ProjectCard: React.FC<ProjectCardProps> = ({
  project,
  onEditClick,
  onDelete,
  isLeader = false,
}) => {
  const navigate = useNavigate();

  return (
    <div
      onClick={() =>
        navigate("/private/tasks", {
          state: { project: project },
        })
      }
      className="flex-shrink-0 w-72 bg-white rounded-xl border border-gray-200 shadow-md hover:shadow-xl transition-shadow duration-300 cursor-pointer flex flex-col relative group"
    >
      <div className="rounded-t-xl h-60 bg-gray-50 flex items-center justify-center overflow-hidden">
        <img
          src={
            project.imageUrl
              ? `http://localhost:8080/blokey-land/app/uploads/${project.imageUrl}`
              : defaultProjectImage
          }
          alt={project.title}
          className="max-h-full max-w-full object-contain"
        />
      </div>

      <div className="p-5 flex flex-col flex-grow relative">
        <h3 className="text-lg font-bold text-gray-800 mb-2 truncate">
          {project.title}
        </h3>
        <p className="text-gray-600 text-sm line-clamp-3 mb-4 flex-grow">
          {project.description || "설명 없음"}
        </p>
        <div className="text-sm text-gray-600 mb-1">
          <span className="inline-block min-w-[3rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
            시작일자
          </span>
          <span className="text-gray-800 font-medium text-sm">
            {project?.actualStartDate || project?.estimatedStartDate || "미상"}
          </span>
        </div>
        <div className="text-sm text-gray-600 mb-1">
          <span className="inline-block min-w-[3rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
            종료일자
          </span>
          <span className="text-gray-800 font-medium text-sm">
            {project?.actualEndDate || project?.estimatedEndDate || "미상"}
          </span>
        </div>

        {isLeader && (
          <div className="absolute bottom-3 right-3 flex space-x-2 z-10 bg-white p-1 rounded-md shadow">
            <button
              onClick={(e) => {
                e.stopPropagation();
                onEditClick && onEditClick(project);
              }}
              aria-label="수정"
              className="p-1 rounded hover:bg-green-100 text-green-600 transition"
            >
              <EditIcon size={16} />
            </button>
            <button
              onClick={(e) => {
                e.stopPropagation();
                onDelete && onDelete(project.id);
              }}
              aria-label="삭제"
              className="p-1 rounded hover:bg-red-100 text-red-600 transition"
            >
              <TrashIcon size={16} />
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProjectCard;
