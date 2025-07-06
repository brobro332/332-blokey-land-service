import React, {
  useState,
  useRef,
  useEffect,
  useMemo,
  useCallback,
} from "react";
import { apiAxios } from "../../../utils/tsx/Api";
import { Milestone } from "../../../types/milestone";
import { FaPlus } from "react-icons/fa";
import { IconBaseProps } from "react-icons";
import * as FaIcons from "react-icons/fa";
import MilestoneFilterBar from "./MilestoneFilterBar";
import MilestoneDetailModal from "./MilestoneDetailModal";

const FlagIcon = FaIcons.FaFlag as React.FC<IconBaseProps>;
const PlusIcon = FaPlus as React.FC<IconBaseProps>;
const ChevronLeftIcon = FaIcons.FaChevronLeft as React.FC<IconBaseProps>;
const ChevronRightIcon = FaIcons.FaChevronRight as React.FC<IconBaseProps>;

interface MilestoneDeckProps {
  milestones: Milestone[];
  getProjectTitle: (projectId: number | undefined) => string;
  onBack: () => void;
  onCreate: () => void;
  displayDate: string;
  isLeader: boolean | undefined;
}

const MilestoneDeck: React.FC<MilestoneDeckProps> = ({
  milestones,
  getProjectTitle,
  onBack,
  onCreate,
  displayDate,
  isLeader,
}) => {
  const pageSize = 5;
  const [visibleCount, setVisibleCount] = useState(pageSize);
  const [searchText, setSearchText] = useState("");
  const [sortKey, setSortKey] = useState<"project" | "title">("title");
  const [sortOrder, setSortOrder] = useState<"asc" | "desc">("asc");
  const [currentMilestones, setCurrentMilestones] = useState(milestones);
  const [selectedMilestone, setSelectedMilestone] = useState<Milestone | null>(
    null
  );
  const containerRef = useRef<HTMLDivElement>(null);

  const sortedMilestones = useMemo(() => {
    const filtered = currentMilestones.filter((ms) => {
      const title = ms.title?.toLowerCase() || "";
      const desc = ms.description?.toLowerCase() || "";
      const s = searchText.toLowerCase();
      return (
        title.includes(s) ||
        desc.includes(s) ||
        getProjectTitle(ms.projectId).includes(s)
      );
    });

    return filtered.sort((a, b) => {
      const aVal =
        sortKey === "project" ? getProjectTitle(a.projectId) : a.title || "";
      const bVal =
        sortKey === "project" ? getProjectTitle(b.projectId) : b.title || "";
      return sortOrder === "asc"
        ? aVal.localeCompare(bVal)
        : bVal.localeCompare(aVal);
    });
  }, [currentMilestones, searchText, sortKey, sortOrder, getProjectTitle]);

  const visibleMilestones = sortedMilestones.slice(0, visibleCount);

  /**
   * @description 마일스톤 수정
   */
  const updateMilestone = useCallback(async (updated: Milestone) => {
    try {
      await apiAxios(`/blokey-land/api/milestones/${updated.id}`, {
        method: "PATCH",
        data: {
          title: updated.title,
          description: updated.description,
        },
        withCredentials: true,
      });
      setCurrentMilestones((prev) =>
        prev.map((m) => (m.id === updated.id ? updated : m))
      );
    } catch (error) {
      console.error(error);
      alert("수정 실패");
    }
  }, []);

  /**
   * @description 마일스톤 삭제
   */
  const deleteMilestone = useCallback(async (msId: number) => {
    try {
      await apiAxios(`/blokey-land/api/milestones/${msId}`, {
        method: "DELETE",
        withCredentials: true,
      });
      setCurrentMilestones((prev) => prev.filter((m) => m.id !== msId));
      setSelectedMilestone(null);
    } catch (error) {
      console.error(error);
      alert("삭제 실패");
    }
  }, []);

  /**
   * @description 마일스톤 목록 초기화
   */
  useEffect(() => {
    console.log("MilestoneDeck milestones changed", milestones);
    setCurrentMilestones(milestones);
  }, [milestones]);

  /**
   * @description 마일스톤 슬라이스 크기 초기화
   */
  useEffect(() => {
    setVisibleCount(pageSize);
  }, [currentMilestones]);

  return (
    <div className="max-w-[1160px] w-full h-full mx-auto p-6 pb-2 relative">
      <div className="flex justify-between items-center mb-2">
        <div>
          <span className="inline-block min-w-[3rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
            마감일자
          </span>
          {displayDate}
        </div>
        <button
          className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
          onClick={onBack}
        >
          뒤로가기
        </button>
      </div>

      <MilestoneFilterBar
        searchText={searchText}
        setSearchText={setSearchText}
        sortKey={sortKey}
        setSortKey={setSortKey}
        sortOrder={sortOrder}
        setSortOrder={setSortOrder}
      />

      <button
        onClick={() => {
          containerRef.current?.scrollBy({ left: -300, behavior: "smooth" });
        }}
        aria-label="왼쪽 스크롤"
        className="absolute top-1/2 left-0 -translate-y-1/2 bg-white border rounded-full p-2 shadow hover:bg-gray-100 z-20 flex items-center justify-center"
      >
        <ChevronLeftIcon size={20} />
      </button>
      <button
        onClick={() => {
          containerRef.current?.scrollBy({ left: 300, behavior: "smooth" });
        }}
        aria-label="오른쪽 스크롤"
        className="absolute top-1/2 right-0 -translate-y-1/2 bg-white border rounded-full p-2 shadow hover:bg-gray-100 z-20 flex items-center justify-center"
      >
        <ChevronRightIcon size={20} />
      </button>

      <div
        className="w-full max-w-full overflow-x-auto bg-gray-100 rounded-xl p-6 scrollbar-hide"
        style={{ minHeight: "300px" }}
        ref={containerRef}
      >
        <div className="flex space-x-6 min-w-full">
          <div
            onClick={onCreate}
            className="flex-shrink-0 w-72 bg-green-100 rounded-xl border-2 border-dashed border-green-400 hover:bg-green-200 hover:border-green-600 text-green-700 cursor-pointer flex flex-col relative group"
          >
            <div className="rounded-t-xl h-40 bg-green-200 flex items-center justify-center overflow-hidden">
              <PlusIcon size={48} />
            </div>
            <div className="p-5 h-40 flex flex-col flex-grow items-center justify-center text-center">
              <h3 className="text-lg font-bold">+ 새 마일스톤 만들기</h3>
            </div>
          </div>

          {visibleMilestones.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-[300px] w-full text-center px-4">
              <div className="mb-6 text-red-500">
                <FlagIcon size={64} />
              </div>
              <h2 className="text-2xl font-bold text-gray-800 mb-2">
                검색된 마일스톤이 없습니다.
              </h2>
              <p className="text-gray-600">지금 새 마일스톤을 만들어보세요!</p>
            </div>
          ) : (
            visibleMilestones.map((ms, i) => (
              <div
                key={`${ms.id ?? `${ms.title}-${i}`}`}
                className="relative min-w-[300px] max-w-xs p-6 border rounded-lg bg-white flex-shrink-0 cursor-pointer"
                onClick={() => setSelectedMilestone(ms)}
              >
                <div className="font-semibold mb-3 flex items-center gap-3">
                  <span className="inline-block min-w-[3rem] bg-green-100 text-green-700 text-xs font-semibold px-3 py-1 rounded-full text-center">
                    {getProjectTitle(ms.projectId)}
                  </span>
                  <span className="text-xl truncate">{ms.title}</span>
                </div>
                <p className="text-gray-700 text-base overflow-hidden line-clamp-6 whitespace-pre-wrap">
                  {ms.description || "설명 없음"}
                </p>
              </div>
            ))
          )}
        </div>
      </div>

      {selectedMilestone && (
        <MilestoneDetailModal
          milestone={selectedMilestone}
          projectTitle={getProjectTitle(selectedMilestone.projectId)}
          onClose={() => setSelectedMilestone(null)}
          onUpdate={updateMilestone}
          onDelete={deleteMilestone}
          isLeader={isLeader}
        />
      )}
    </div>
  );
};

export default MilestoneDeck;
