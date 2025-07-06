export type Menu =
  | "private-dashboard"
  | "private-projects"
  | "private-milestones"
  | "community-projects";

export type ViewMode = "private" | "community";
export type PriorityType = "LOW" | "MEDIUM" | "HIGH";
export type TaskStatusType = "TODO" | "IN_PROGRESS" | "REVIEW" | "DONE";
export type OfferStatusType = "PENDING" | "ACCEPTED" | "REJECTED";
export type OfferType = "PROJECT" | "BLOKEY";
export type RoleType = "LEADER" | "MANAGER" | "MEMBER" | "VIEWER";
