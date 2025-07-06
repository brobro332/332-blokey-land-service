import React, { useEffect, useState, useCallback } from "react";
import { apiAxios } from "../../utils/tsx/Api";
import { Milestone } from "../../types/milestone";
import { Project } from "../../types/project";
import MilestoneCalendar from "../../components/private/milestones/MilestoneCalendar";

const MilestonesPage: React.FC = () => {
  const [milestones, setMilestones] = useState<Milestone[]>([]);
  const [projects, setProjects] = useState<Project[]>([]);
  const [selectedProject, setSelectedProject] = useState<Project | undefined>(
    undefined
  );

  /**
   * @description 마일스톤 생성
   */
  const createMilestone = async (data: {
    title: string;
    description: string;
    dueDate: string;
    projectId: number;
  }) => {
    try {
      await apiAxios(`/blokey-land/api/projects/${data.projectId}/milestones`, {
        method: "POST",
        data,
        withCredentials: true,
      });

      await fetchMilestones();
    } catch {
      alert("마일스톤 생성 실패");
    }
  };

  /**
   * @description 마일스톤 목록 조회
   */
  const fetchMilestones = useCallback(async () => {
    try {
      const url = selectedProject
        ? `/blokey-land/api/projects/${selectedProject.id}/milestones`
        : `/blokey-land/api/milestones`;

      const res = await apiAxios(url, {
        method: "GET",
        withCredentials: true,
      });

      setMilestones(res || []);
    } catch {
      setMilestones([]);
    }
  }, [selectedProject]);

  /**
   * @description 프로젝트 목록 초기화
   */
  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const res = await apiAxios("/blokey-land/api/projects/all", {
          method: "GET",
          withCredentials: true,
        });

        const filtered = (res || []).filter(
          (project: Project) => project.status !== "DELETED"
        );

        setProjects(filtered);
      } catch {
        setProjects([]);
      }
    };
    fetchProjects();
  }, []);

  /**
   * @description 마일스톤 목록 초기화
   */
  useEffect(() => {
    fetchMilestones();
  }, [fetchMilestones]);

  return (
    <MilestoneCalendar
      milestones={milestones}
      projects={projects}
      selectedProject={selectedProject}
      onProjectChange={setSelectedProject}
      onCreate={createMilestone}
      refetchMilestones={fetchMilestones}
    />
  );
};

export default MilestonesPage;
