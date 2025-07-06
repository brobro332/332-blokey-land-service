import { useEffect, useState } from "react";
import { Task } from "../components/private/dashboard/GanttWithSummary";
import { apiAxios } from "../utils/tsx/Api";

const useTasks = (projects: { id: number }[]) => {
  const [tasks, setTasks] = useState<Task[]>([]);

  useEffect(() => {
    if (!projects.length) {
      setTasks([]);
      return;
    }

    const fetchTasksForProjects = async () => {
      try {
        const tasksByProject = await Promise.all(
          projects.map(async (project) => {
            const response = await apiAxios<Task[]>(
              `/blokey-land/api/projects/${project.id}/tasks/all`,
              {
                method: "GET",
                withCredentials: true,
              }
            );
            return response || [];
          })
        );

        const allTasks = tasksByProject.flat();

        setTasks(allTasks);
      } catch (error) {
        console.error("태스크 목록 불러오기 실패", error);
        setTasks([]);
      }
    };

    fetchTasksForProjects();
  }, [projects]);

  return tasks;
};

export default useTasks;
