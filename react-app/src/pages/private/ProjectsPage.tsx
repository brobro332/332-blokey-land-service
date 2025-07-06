import { useCallback, useEffect, useState } from "react";
import { Project } from "../../types/project";
import { apiAxios } from "../../utils/tsx/Api";
import ProjectDeck from "../../components/private/projects/ProjectDeck";

const ProjectsPage: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [page, setPage] = useState(0);
  const [hasNext, setHasNext] = useState(true);

  /**
   * @description 프로젝트 페이지 목록 조회
   */
  const fetchProjects = useCallback(async (page: number) => {
    try {
      const res = await apiAxios<{ content: Project[]; last: boolean }>(
        `/blokey-land/api/projects/slice?page=${page}&size=10`,
        { method: "GET", withCredentials: true }
      );

      const newProjects = res.content || [];
      const hasNext = !res.last;

      setProjects((prev) => {
        const existings = new Set(prev.map((p) => p.id));
        const uniqueNewProjects = (newProjects as Project[]).filter(
          (p) => !existings.has(p.id)
        );
        return [...prev, ...uniqueNewProjects];
      });
      setHasNext(hasNext);
      setPage(page);
    } catch (e) {
      if (process.env.NODE_ENV === "development") {
        console.error("내 프로젝트 불러오기 실패", e);
      } else {
        alert("프로젝트를 불러오지 못했습니다.");
      }
    }
  }, []);

  /**
   * @description 프로젝트 등록
   */
  const createProject = async (
    title: string,
    description: string,
    imageUrl: string,
    isPrivate: "public" | "private",
    estimatedStartDate: string,
    estimatedEndDate: string,
    actualStartDate: string,
    actualEndDate: string
  ) => {
    try {
      await apiAxios("/blokey-land/api/projects", {
        method: "POST",
        data: {
          title,
          description,
          imageUrl,
          isPrivate,
          estimatedStartDate,
          estimatedEndDate,
          actualStartDate,
          actualEndDate,
        },
        withCredentials: true,
      });
      await resetProjects();
    } catch (e) {
      console.error("프로젝트 생성 실패", e);
    }
  };

  /**
   * @description 프로젝트 수정
   */
  const updateProject = async (project: Project) => {
    try {
      await apiAxios(`/blokey-land/api/projects/${project.id}`, {
        method: "PATCH",
        data: {
          ...project,
        },
        withCredentials: true,
      });
      await resetProjects();
    } catch (e) {
      console.error("프로젝트 수정 실패", e);
      alert("프로젝트 수정에 실패했습니다.");
    }
  };

  /**
   * @description 프로젝트 삭제
   */
  const deleteProject = async (id: number) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    try {
      await apiAxios(`/blokey-land/api/projects/${id}`, {
        method: "DELETE",
        withCredentials: true,
      });
      setProjects((prev) => prev.filter((p) => p.id !== id));
    } catch (e) {
      console.error("삭제 실패", e);
      alert("삭제에 실패했습니다.");
    }
  };

  /**
   * @description 프로젝트 페이지 초기화
   */
  const resetProjects = useCallback(async () => {
    setProjects([]);
    setPage(0);
    setHasNext(true);
    setTimeout(() => fetchProjects(0), 0);
  }, [fetchProjects]);

  /**
   * @description 프로젝트 목록 초기화
   */
  useEffect(() => {
    resetProjects();
  }, [resetProjects]);

  return (
    <ProjectDeck
      projects={projects}
      onCreate={createProject}
      onUpdate={updateProject}
      onDelete={deleteProject}
      onLoadNext={() => fetchProjects(page + 1)}
      hasNext={hasNext}
    />
  );
};

export default ProjectsPage;
