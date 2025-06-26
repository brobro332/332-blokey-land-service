import React, { useState, useEffect } from "react";
import Sidebar from "../components/Sidebar";
import Header from "../components/Header";
import ProjectList from "../components/ProjectList";
import ProjectModal from "../components/ProjectModal";
import { apiAxios } from "../utils/Api";

type ViewMode = "mine" | "community";

const MainPage: React.FC = () => {
  const [viewMode, setViewMode] = useState<ViewMode>("mine");
  const [myProjects, setMyProjects] = useState<any[]>([]);
  const [communityProjects, setCommunityProjects] = useState<any[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    if (viewMode === "mine") {
      fetchMyProjects();
    } else {
      fetchCommunityProjects();
    }
  }, [viewMode]);

  const fetchMyProjects = async () => {
    try {
      const res = await apiAxios("/blokey-land/api/projects/mine", {
        method: "GET",
        withCredentials: true,
      });
      setMyProjects(res.data || []);
    } catch (e) {
      console.error("내 프로젝트 불러오기 실패", e);
      setMyProjects([]);
    }
  };

  const fetchCommunityProjects = async () => {
    try {
      const res = await apiAxios("/blokey-land/api/projects/community", {
        method: "GET",
        withCredentials: true,
      });
      setCommunityProjects(res.data || []);
    } catch (e) {
      console.error("커뮤니티 프로젝트 불러오기 실패", e);
      setCommunityProjects([]);
    }
  };

  const createProject = async (
    title: string,
    description: string,
    estimatedStartDate: string,
    estimatedEndDate: string,
    actualStartDate: string,
    actualEndDate: string
  ) => {
    try {
      await apiAxios("/blokey-land/api/projects", {
        method: "POST",
        data: {
          title,
          description,
          estimatedStartDate,
          estimatedEndDate,
          actualStartDate,
          actualEndDate,
        },
        withCredentials: true,
      });
      fetchMyProjects();
      setIsModalOpen(false);
    } catch (e) {
      console.error("프로젝트 생성 실패", e);
    }
  };

  return (
    <div className="font-stardust flex h-screen bg-gray-50">
      <Sidebar viewMode={viewMode} />

      <div className="flex-1 flex flex-col">
        <Header viewMode={viewMode} setViewMode={setViewMode} />

        <main className="flex-1 bg-gray-50 flex flex-col items-center justify-center p-8 overflow-auto">
          <ProjectList
            projects={viewMode === "mine" ? myProjects : communityProjects}
            isMine={viewMode === "mine"}
            onCreate={() => setIsModalOpen(true)}
          />
        </main>
      </div>

      <ProjectModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onCreate={createProject}
      />
    </div>
  );
};

export default MainPage;
