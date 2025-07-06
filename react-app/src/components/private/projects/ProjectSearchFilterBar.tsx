import React, { useState } from "react";

interface ProjectSearchFilterBarProps {
  searchText: string;
  setSearchText: (text: string) => void;
  filterStatus: "ALL" | "ACTIVE" | "COMPLETED" | "DELETED";
  setFilterStatus: (status: "ALL" | "ACTIVE" | "COMPLETED" | "DELETED") => void;
  sortKey:
    | "title"
    | "estimatedStartDate"
    | "estimatedEndDate"
    | "actualStartDate"
    | "actualEndDate";
  setSortKey: (key: any) => void;
  sortOrder: "asc" | "desc";
  setSortOrder: (order: "asc" | "desc") => void;
}

const sortLabels: Record<string, string> = {
  title: "제목순",
  estimatedStartDate: "예상 시작일순",
  estimatedEndDate: "예상 종료일순",
  actualStartDate: "실제 시작일순",
  actualEndDate: "실제 종료일순",
};

const statusLabels: Record<string, string> = {
  ALL: "전체",
  ACTIVE: "진행중",
  COMPLETED: "완료",
  DELETED: "삭제됨",
};

const ProjectSearchFilterBar: React.FC<ProjectSearchFilterBarProps> = ({
  searchText,
  setSearchText,
  filterStatus,
  setFilterStatus,
  sortKey,
  setSortKey,
  sortOrder,
  setSortOrder,
}) => {
  const [showStatusOptions, setShowStatusOptions] = useState(false);
  const [showSortOptions, setShowSortOptions] = useState(false);

  /**
   * @description 상태 옵션 토글
   */
  const toggleStatusOptions = () => {
    setShowStatusOptions((prev) => {
      if (!prev) setShowSortOptions(false);
      return !prev;
    });
  };

  /**
   * @description 정렬 옵션 토글
   */
  const toggleSortOptions = () => {
    setShowSortOptions((prev) => {
      if (!prev) setShowStatusOptions(false);
      return !prev;
    });
  };

  return (
    <div className="mb-2 p-4 bg-gray-100 rounded-xl shadow-sm flex flex-wrap gap-3 items-center relative z-20">
      <input
        type="text"
        placeholder="프로젝트 간편검색 (제목, 설명)"
        className="border border-gray-300 rounded-full px-4 py-2 text-sm focus:ring-2 focus:ring-green-400 flex-grow min-w-[200px]"
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
      />

      <div className="relative">
        <button
          onClick={toggleStatusOptions}
          className="border border-gray-300 rounded-full px-4 py-2 text-sm bg-white hover:bg-gray-100"
          type="button"
        >
          상태: {statusLabels[filterStatus]}
        </button>
        {showStatusOptions && (
          <div className="absolute z-50 mt-1 bg-white border border-gray-200 rounded shadow text-sm w-36">
            {Object.entries(statusLabels).map(([key, label]) => (
              <div
                key={key}
                onClick={() => {
                  setFilterStatus(key as any);
                  setShowStatusOptions(false);
                }}
                className="px-4 py-2 hover:bg-green-50 cursor-pointer"
              >
                {label}
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="relative">
        <button
          onClick={toggleSortOptions}
          className="border border-gray-300 rounded-full px-4 py-2 text-sm bg-white hover:bg-gray-100"
          type="button"
        >
          정렬: {sortLabels[sortKey]} (
          {sortOrder === "asc" ? "오름차순" : "내림차순"})
        </button>
        {showSortOptions && (
          <div className="absolute z-50 mt-1 bg-white border border-gray-200 rounded shadow text-sm w-56">
            {Object.entries(sortLabels).map(([key, label]) => (
              <div
                key={key}
                onClick={() => {
                  setSortKey(key as any);
                  setShowSortOptions(false);
                }}
                className="px-4 py-2 hover:bg-green-50 cursor-pointer"
              >
                {label}
              </div>
            ))}
            <div className="px-4 py-2 border-t">
              <button
                onClick={() =>
                  setSortOrder(sortOrder === "asc" ? "desc" : "asc")
                }
                className="text-green-600 hover:underline"
              >
                방향: {sortOrder === "asc" ? "오름차순" : "내림차순"}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProjectSearchFilterBar;
