import React from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  Legend,
} from "recharts";
import { Task } from "../../../types/task";
import { Milestone } from "../../../types/milestone";

interface Project {
  id: number;
  title: string;
}

interface ResourceCountBarChartProps {
  task: Task[];
  milestones: Milestone[];
  projects: Project[];
  selectedProjectId: number;
}

const ResourceCountBarChart: React.FC<ResourceCountBarChartProps> = ({
  task,
  milestones,
  projects,
  selectedProjectId,
}) => {
  const project = projects.find((p) => p.id === selectedProjectId);

  if (!project) {
    return <div className="text-center text-gray-500">프로젝트 없음</div>;
  }

  const taskCount = task.filter((t) => t.projectId === project.id).length;
  const milestoneCount = milestones.filter(
    (m) => m.projectId === project.id
  ).length;

  const data = [
    {
      name: project.title,
      taskCount,
      milestoneCount,
    },
  ];

  return (
    <ResponsiveContainer width="100%" height={300}>
      <BarChart
        data={data}
        margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
        barCategoryGap={5}
        barGap={10}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis allowDecimals={false} />
        <Tooltip />
        <Legend />
        <Bar dataKey="milestoneCount" fill="#60a5fa" name="마일스톤 개수" />
        <Bar dataKey="taskCount" fill="#4ade80" name="태스크 개수" />
      </BarChart>
    </ResponsiveContainer>
  );
};

export default ResourceCountBarChart;
