import { Menu } from "../../types/common";

export const menuToPath: Record<string, string> = {
  /* PRIVATE */
  "private-dashboard": "/private/dashboard",
  "private-projects": "/private/projects",
  "private-milestones": "/private/milestones",

  /* COMMNUNITY */
  "community-projects": "/community/projects",
};

export const getSelectedMenuFromPath = (pathname: string): Menu => {
  /* PRIVATE */
  if (pathname.startsWith("/private/projects")) return "private-projects";
  if (pathname.startsWith("/private/milestones")) return "private-milestones";
  if (pathname.startsWith("/private/dashboard")) return "private-dashboard";

  /* COMMNUNITY */
  if (pathname.startsWith("/community/dashboard")) return "community-projects";
  return "private-dashboard";
};
