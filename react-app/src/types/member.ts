export interface Member {
  id: number;
  blokeyId: string;
  role: "LEADER" | "MANAGER" | "MEMBER" | "VIEWER";
  nickname: string;
  bio: string;
}
