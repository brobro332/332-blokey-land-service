import { PriorityType, TaskStatusType } from "./common";

export interface Task {
  id: number;
  title: string;
  description?: string;
  progress?: number;
  status: TaskStatusType;
  priority?: PriorityType;
  estimatedStartDate?: string;
  estimatedEndDate?: string;
  actualStartDate?: string;
  actualEndDate?: string;
  projectId?: number;
  milestoneId?: number;
}
