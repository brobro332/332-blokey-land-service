import React, { useState, useRef, useEffect, useCallback } from "react";
import ProjectSearchFilterBar from "./ProjectSearchFilterBar";
import ProjectCard from "./ProjectCard";
import ProjectEmptyState from "./ProjectEmptyState";
import ProjectConditionalSearchModal from "./ProjectConditionalSearchModal";
import { IconBaseProps } from "react-icons";
import * as FaIcons from "react-icons/fa";
import { Project } from "../../../types/project";
import ProjectFormModal from "./ProjectFormModal";

const PlusIcon = FaIcons.FaPlus as React.FC<IconBaseProps>;
const LeftIcon = FaIcons.FaChevronLeft as React.FC<IconBaseProps>;
const RightIcon = FaIcons.FaChevronRight as React.FC<IconBaseProps>;

interface ProjectsProps {
  projects: Project[];
  onCreate?: (
    title: string,
    description: string,
    imageUrl: string,
    isPrivate: "public" | "private",
    estimatedStartDate: string,
    estimatedEndDate: string,
    actualStartDate: string,
    actualEndDate: string
  ) => Promise<void>;
  onUpdate?: (project: Project) => Promise<void>;
  onDelete?: (projectId: number) => void;
  onLoadNext?: () => void;
  hasNext?: boolean;
}

const ProjectDeck: React.FC<ProjectsProps> = ({
  projects,
  onCreate,
  onUpdate,
  onDelete,
  onLoadNext,
  hasNext,
}) => {
  const [editProject, setEditProject] = useState<Project | null>(null);
  const [isSubmitModalOpen, setIsSubmitModalOpen] = useState(false);
  const [isConditionalSearchModalOpen, setIsConditionalSearchModalOpen] =
    useState(false);
  const [searchText, setSearchText] = useState("");
  const [filterStatus, setFilterStatus] = useState<
    "ALL" | "ACTIVE" | "COMPLETED" | "DELETED"
  >("ACTIVE");
  const [sortKey, setSortKey] = useState<
    | "title"
    | "estimatedStartDate"
    | "estimatedEndDate"
    | "actualStartDate"
    | "actualEndDate"
  >("title");
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");

  const loadingRef = useRef(false);
  const containerRef = useRef<HTMLDivElement>(null);

  /**
   * @description 로딩 상태 저장
   */
  const setLoadingState = (value: boolean) => {
    loadingRef.current = value;
  };

  /**
   * @description 생성 버튼 클릭
   */
  const handleAddClick = () => {
    setIsSubmitModalOpen(true);
  };

  /**
   * @description 수정 버튼 클릭
   */
  const handleEditClick = (project: Project) => {
    setEditProject(project);
    setIsSubmitModalOpen(true);
  };

  /**
   * @description 간편검색 결과 목록
   */
  const filteredProjects = projects
    .filter((p) => {
      if (filterStatus !== "ALL" && p.status !== filterStatus) return false;
      const lower = searchText.toLowerCase();
      return (
        p.title.toLowerCase().includes(lower) ||
        (p.description && p.description.toLowerCase().includes(lower))
      );
    })
    .sort((a, b) => {
      const getValue = (project: Project) => {
        switch (sortKey) {
          case "title":
            return project.title.toLowerCase();
          case "estimatedStartDate":
            return project.estimatedStartDate || "";
          case "estimatedEndDate":
            return project.estimatedEndDate || "";
          case "actualStartDate":
            return project.actualStartDate || "";
          case "actualEndDate":
            return project.actualEndDate || "";
          default:
            return "";
        }
      };
      const aVal = getValue(a);
      const bVal = getValue(b);
      if (aVal < bVal) return sortOrder === "asc" ? -1 : 1;
      if (aVal > bVal) return sortOrder === "asc" ? 1 : -1;
      return 0;
    });

  /**
   * @description 화면 왼쪽 스크롤
   */
  const scrollLeft = () => {
    containerRef.current?.scrollBy({ left: -300, behavior: "smooth" });
  };

  /**
   * @description 화면 오른쪽 스크롤
   */
  const scrollRight = () => {
    containerRef.current?.scrollBy({ left: 300, behavior: "smooth" });
  };

  /**
   * @description 다음 페이지 데이터 로드
   */
  const handleScroll = useCallback(() => {
    const el = containerRef.current;
    if (!el || !hasNext || !onLoadNext) return;

    const scrollRightEdge = el.scrollWidth - el.scrollLeft - el.clientWidth;
    if (scrollRightEdge < 50) {
      if (loadingRef.current) return;
      setLoadingState(true);
      Promise.resolve(onLoadNext()).finally(() => setLoadingState(false));
    }
  }, [hasNext, onLoadNext]);

  /**
   * @description 스크롤 이벤트 초기화
   */
  useEffect(() => {
    const el = containerRef.current;
    if (!el) return;

    el.addEventListener("scroll", handleScroll);
    return () => el.removeEventListener("scroll", handleScroll);
  }, [handleScroll]);

  return (
    <div className="w-full max-w-[1185px] relative">
      <div className="flex items-center justify-between">
        <div className="flex-grow min-w-[300px]">
          <ProjectSearchFilterBar
            searchText={searchText}
            setSearchText={setSearchText}
            filterStatus={filterStatus}
            setFilterStatus={setFilterStatus}
            sortKey={sortKey}
            setSortKey={setSortKey}
            sortOrder={sortOrder}
            setSortOrder={setSortOrder}
          />
        </div>
        <button
          className="mb-1 ml-4 px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700 min-h-[68px] max-h-[68px]"
          onClick={() => setIsConditionalSearchModalOpen(true)}
          type="button"
        >
          조건검색
        </button>
      </div>

      <button
        onClick={scrollLeft}
        aria-label="왼쪽 스크롤"
        className="absolute top-1/2 left-0 -translate-y-1/2 bg-white border rounded-full p-2 shadow hover:bg-gray-100 z-20 flex items-center justify-center"
      >
        <LeftIcon size={20} />
      </button>
      <button
        onClick={scrollRight}
        aria-label="오른쪽 스크롤"
        className="absolute top-1/2 right-0 -translate-y-1/2 bg-white border rounded-full p-2 shadow hover:bg-gray-100 z-20 flex items-center justify-center"
      >
        <RightIcon size={20} />
      </button>

      <div
        className="flex space-x-6 overflow-x-auto py-6 scrollbar-hide bg-gray-100 rounded-xl p-6 min-h-[510px]"
        ref={containerRef}
      >
        <div
          onClick={handleAddClick}
          className="flex-shrink-0 w-72 bg-green-100 rounded-xl border-2 border-dashed border-green-400 hover:bg-green-200 hover:border-green-600 text-green-700 cursor-pointer flex flex-col relative group"
        >
          <div className="rounded-t-xl h-60 bg-green-200 flex items-center justify-center overflow-hidden">
            <PlusIcon size={48} />
          </div>
          <div className="p-5 flex flex-col flex-grow items-center justify-center text-center">
            <h3 className="text-lg font-bold">새 프로젝트 만들기</h3>
          </div>
        </div>

        {filteredProjects.length === 0 && (
          <div className="flex justify-center items-center w-full">
            <ProjectEmptyState />
          </div>
        )}

        {filteredProjects.map((project) => (
          <ProjectCard
            key={project.id}
            project={project}
            onEditClick={handleEditClick}
            onDelete={onDelete}
            isLeader={project.leader}
          />
        ))}
      </div>

      <ProjectConditionalSearchModal
        isOpen={isConditionalSearchModalOpen}
        onClose={() => setIsConditionalSearchModalOpen(false)}
      />

      <ProjectFormModal
        isOpen={isSubmitModalOpen}
        onClose={() => {
          setIsSubmitModalOpen(false);
          setEditProject(null);
        }}
        mode={editProject ? "edit" : "create"}
        initialData={
          editProject
            ? {
                ...editProject,
                imageUrl: editProject.imageUrl ?? "",
                description: editProject.description ?? "",
                estimatedStartDate: editProject.estimatedStartDate ?? "",
                estimatedEndDate: editProject.estimatedEndDate ?? "",
                actualStartDate: editProject.actualStartDate ?? "",
                actualEndDate: editProject.actualEndDate ?? "",
                isPrivate: editProject.isPrivate ? "private" : "public",
              }
            : undefined
        }
        onCreate={onCreate}
        onUpdate={onUpdate}
      />
    </div>
  );
};

export default ProjectDeck;
