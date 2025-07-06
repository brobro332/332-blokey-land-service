import React from "react";
import SimpleGantt from "../../components/private/dashboard/GanttWithSummary";
import useProjects from "../../hooks/useProjects";
import useTasks from "../../hooks/useTasks";

const DashboardPage: React.FC = () => {
  const projects = useProjects();
  const tasks = useTasks(projects);

  return (
    <div style={{ width: "100%" }}>
      <SimpleGantt projects={projects} tasks={tasks} />
    </div>
  );
};

export default DashboardPage;
