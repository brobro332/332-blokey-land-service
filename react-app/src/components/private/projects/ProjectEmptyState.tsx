import React from "react";
import * as FaIcons from "react-icons/fa";
import { IconBaseProps } from "react-icons";

const FolderOpenIcon = FaIcons.FaFolderOpen as React.FC<IconBaseProps>;

const ProjectEmptyState: React.FC = () => {
  return (
    <div className="flex flex-col items-center justify-center py-20 text-center px-4">
      <div className="mb-6 text-yellow-500">
        <FolderOpenIcon size={64} />
      </div>
      <h2 className="text-2xl font-bold text-gray-800 mb-2 text-center">
        아직 프로젝트가 없습니다.
      </h2>
      <p className="text-gray-600 mb-6">지금 새 프로젝트를 만들어보세요!</p>
    </div>
  );
};

export default ProjectEmptyState;
