import { useEffect, useState } from "react";
import { apiAxios } from "../utils/tsx/Api";
import { Task } from "../types/task";

interface Project {
  id: number;
  title: string;
  tasks: Task[];
}

const useProjects = () => {
  const [projects, setProjects] = useState<Project[]>([]);

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        const res = await apiAxios("/blokey-land/api/projects/all-with-tasks", {
          method: "GET",
          withCredentials: true,
        });

        const simplifiedProjects = res
          .filter((p: any) => p.status !== "DELETED")
          .map((p: any) => ({
            id: p.id,
            title: p.title,
            tasks: p.tasks,
          }));

        setProjects(simplifiedProjects);
      } catch (error) {
        console.error("프로젝트 목록 불러오기 실패", error);
        setProjects([]);
      }
    };

    fetchProjects();
  }, []);

  return projects;
};

export default useProjects;
