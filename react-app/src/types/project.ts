export interface Project {
  id: number;
  title: string;
  description: string;
  imageUrl?: string;
  status?: "ACTIVE" | "COMPLETED" | "DELETED";
  isPrivate: string;
  leader?: boolean;
  estimatedStartDate: string;
  estimatedEndDate: string;
  actualStartDate: string;
  actualEndDate: string;
}
