import React, { useEffect, useState } from "react";
import GanttWithSummary from "../../components/private/dashboard/GanttWithSummary";
import useProjects from "../../hooks/useProjects";
import useMilestones from "../../hooks/useMilestones";

const DashboardPage: React.FC = () => {
  const projects = useProjects();
  const [selectedProjectId, setSelectedProjectId] = useState<number | null>(
    null
  );

  useEffect(() => {
    if (projects.length > 0 && selectedProjectId === null) {
      setSelectedProjectId(projects[0].id);
    }
  }, [projects, selectedProjectId]);

  const milestones = useMilestones({
    selectedProjectId: selectedProjectId ?? undefined,
  });

  return (
    <div style={{ width: "100%" }}>
      {selectedProjectId && (
        <GanttWithSummary
          projects={projects}
          selectedProjectId={selectedProjectId}
          setSelectedProjectId={setSelectedProjectId}
          milestones={milestones}
        />
      )}
    </div>
  );
};

export default DashboardPage;
