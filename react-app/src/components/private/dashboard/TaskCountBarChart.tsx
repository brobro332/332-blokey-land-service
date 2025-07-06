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
import { Task } from "./GanttWithSummary";

interface Project {
  id: number;
  title: string;
}

interface TaskCountBarChartProps {
  tasks: Task[];
  projects: Project[];
}

const TaskCountBarChart: React.FC<TaskCountBarChartProps> = ({
  tasks,
  projects,
}) => {
  const hasData = projects.length > 0 && tasks.length > 0;

  const data = hasData
    ? projects.map((project) => ({
        name: project.title,
        taskCount: tasks.filter((t) => t.projectId === project.id).length,
      }))
    : [{ name: "데이터 없음", taskCount: 0 }];

  return (
    <ResponsiveContainer width="100%" height={300}>
      <BarChart
        data={data}
        margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis allowDecimals={false} />
        <Tooltip />
        <Legend />
        <Bar
          dataKey="taskCount"
          fill={hasData ? "#4ade80" : "#d1d5db"}
          name="태스크 개수"
        />
      </BarChart>
    </ResponsiveContainer>
  );
};

export default TaskCountBarChart;
