export interface Project {
  id: number;
  title: string;
  description: string;
  imageUrl?: string;
  status?: "ACTIVE" | "COMPLETED" | "DELETED";
  isPrivate: boolean;
  isLeader?: boolean;
  estimatedStartDate: string;
  estimatedEndDate: string;
  actualStartDate: string;
  actualEndDate: string;
}
