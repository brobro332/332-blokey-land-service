import React, { useState } from "react";

interface MilestoneFilterBarProps {
  searchText: string;
  setSearchText: (text: string) => void;
  sortKey: "title" | "project";
  setSortKey: (key: "title" | "project") => void;
  sortOrder: "asc" | "desc";
  setSortOrder: (order: "asc" | "desc") => void;
}

const sortLabels: Record<string, string> = {
  title: "제목순",
  project: "프로젝트명순",
};

const MilestoneFilterBar: React.FC<MilestoneFilterBarProps> = ({
  searchText,
  setSearchText,
  sortKey,
  setSortKey,
  sortOrder,
  setSortOrder,
}) => {
  const [showSortOptions, setShowSortOptions] = useState(false);

  return (
    <div className="mb-2 p-4 bg-gray-100 rounded-xl shadow-sm flex flex-wrap gap-3 items-center relative z-20">
      <input
        type="text"
        placeholder="마일스톤 간편검색 (제목, 설명, 프로젝트)"
        className="border border-gray-300 rounded-full px-4 py-2 text-sm flex-grow min-w-[200px] focus:ring-2 focus:ring-green-400"
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
      />

      <div className="relative">
        <button
          onClick={(prev) => setShowSortOptions(!prev)}
          className="border border-gray-300 rounded-full px-4 py-2 text-sm bg-white hover:bg-gray-100"
          type="button"
        >
          정렬: {sortLabels[sortKey]} (
          {sortOrder === "asc" ? "오름차순" : "내림차순"})
        </button>

        {showSortOptions && (
          <div className="absolute z-50 mt-1 bg-white border border-gray-200 rounded shadow text-sm w-48">
            {Object.entries(sortLabels).map(([key, label]) => (
              <div
                key={key}
                onClick={() => {
                  setSortKey(key as "title" | "project");
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

export default MilestoneFilterBar;
