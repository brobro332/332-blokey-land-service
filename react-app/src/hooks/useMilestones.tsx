import { useEffect, useState } from "react";
import { apiAxios } from "../utils/tsx/Api";
import { Milestone } from "../types/milestone";

interface UseMilestonesProps {
  selectedProjectId?: number;
}

const useMilestones = ({ selectedProjectId }: UseMilestonesProps) => {
  const [milestones, setMilestones] = useState<Milestone[]>([]);

  useEffect(() => {
    const fetchMilestones = async () => {
      try {
        const url = selectedProjectId
          ? `/blokey-land/api/projects/${selectedProjectId}/milestones`
          : `/blokey-land/api/milestones`;

        const res = await apiAxios(url, {
          method: "GET",
          withCredentials: true,
        });

        setMilestones(res || []);
      } catch (error) {
        console.error("마일스톤 목록 불러오기 실패", error);
        setMilestones([]);
      }
    };

    fetchMilestones();
  }, [selectedProjectId]);

  return milestones;
};

export default useMilestones;
