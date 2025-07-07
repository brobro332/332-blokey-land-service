import React, { useState } from "react";
import { Project } from "../../../types/project";
import { apiAxios } from "../../../utils/tsx/Api";
import ProjectTable from "./ProjectTable";

interface ProjectConditionalSearchModalProps {
  isOpen: boolean;
  onClose: () => void;
}

const ProjectConditionalSearchModal: React.FC<
  ProjectConditionalSearchModalProps
> = ({ isOpen, onClose }) => {
  const [filters, setFilters] = useState({
    title: "",
    description: "",
    actualStartFrom: "",
    actualEndTo: "",
    status: "",
    isPrivate: "",
  });
  const [searchResults, setSearchResults] = useState<Project[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(5);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  /**
   * @description 필터 상태 변경
   */
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  /**
   * @description 필터 초기화
   */
  const handleReset = () => {
    setFilters({
      title: "",
      description: "",
      actualStartFrom: "",
      actualEndTo: "",
      status: "",
      isPrivate: "",
    });
    setSearchResults([]);
    setError(null);
    setPageNumber(0);
    setTotalPages(0);
    setTotalElements(0);
  };

  /**
   * @description 조건검색
   */
  const handleSearch = async (page = 0) => {
    setLoading(true);
    setError(null);

    try {
      const params = new URLSearchParams();

      Object.entries(filters).forEach(([key, val]) => {
        if (val !== undefined && val !== null && val !== "") {
          params.append(key, val);
        }
      });

      params.append("page", page.toString());
      params.append("size", pageSize.toString());

      const res = await apiAxios(
        `/blokey-land/api/projects/page?${params.toString()}`,
        {
          method: "GET",
          withCredentials: true,
        }
      );

      const data = res.data || res;
      const content = data.content || [];
      const pageable = data.pageable || {};

      setSearchResults(content);
      setPageNumber(pageable.pageNumber ?? 0);
      setPageSize(pageable.pageSize ?? 10);
      setTotalPages(data.totalPages ?? 0);
      setTotalElements(data.totalElements ?? 0);
    } catch (err) {
      console.error("검색 실패:", err);
      setError("검색 중 오류가 발생했습니다.");
      setSearchResults([]);
      setPageNumber(0);
      setTotalPages(0);
      setTotalElements(0);
    } finally {
      setLoading(false);
    }
  };

  /**
   * @description 페이지 이동
   */
  const moveToPage = (page: number) => {
    if (page < 0 || page >= totalPages) return;
    handleSearch(page);
  };

  if (!isOpen) return null;

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50"
      aria-modal="true"
      role="dialog"
      onClick={onClose}
    >
      <div
        className="bg-white rounded-lg shadow-lg p-6 w-full max-w-5xl max-h-[90vh] overflow-y-auto"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className="text-xl font-bold mb-2 border-b-2 border-green-400 text-green-700">
          프로젝트 조건검색
        </h2>

        <div className="grid grid-cols-2 gap-2 bg-gray-100 rounded-xl p-4 shadow-sm">
          <div>
            <label htmlFor="title" className="block mb-1 text-sm font-medium">
              프로젝트 제목
            </label>
            <input
              id="title"
              name="title"
              value={filters.title}
              onChange={handleChange}
              placeholder="프로젝트 제목"
              className="border p-2 rounded w-full"
            />
          </div>
          <div>
            <label
              htmlFor="description"
              className="block mb-1 text-sm font-medium"
            >
              프로젝트 설명
            </label>
            <input
              id="description"
              name="description"
              value={filters.description}
              onChange={handleChange}
              placeholder="프로젝트 설명"
              className="border p-2 rounded w-full"
            />
          </div>
          <div>
            <label
              htmlFor="actualStartFrom"
              className="block mb-1 text-sm font-medium"
            >
              실제진행일자 (FROM)
            </label>
            <input
              id="actualStartFrom"
              type="date"
              name="actualStartFrom"
              value={filters.actualStartFrom}
              onChange={handleChange}
              className="border p-2 rounded w-full"
            />
          </div>
          <div>
            <label
              htmlFor="actualEndTo"
              className="block mb-1 text-sm font-medium"
            >
              실제진행일자 (TO)
            </label>
            <input
              id="actualEndTo"
              type="date"
              name="actualEndTo"
              value={filters.actualEndTo}
              onChange={handleChange}
              className="border p-2 rounded w-full"
            />
          </div>
          <div>
            <label htmlFor="status" className="block mb-1 text-sm font-medium">
              상태
            </label>
            <select
              id="status"
              name="status"
              value={filters.status}
              onChange={handleChange}
              className="border p-2 rounded w-full"
            >
              <option value="">상태 선택</option>
              <option value="ACTIVE">진행중</option>
              <option value="COMPLETED">완료</option>
              <option value="DELETED">삭제됨</option>
            </select>
          </div>
          <div>
            <label
              htmlFor="isPrivate"
              className="block mb-1 text-sm font-medium"
            >
              공개 여부
            </label>
            <select
              id="isPrivate"
              name="isPrivate"
              value={filters.isPrivate}
              onChange={handleChange}
              className="border p-2 rounded w-full"
            >
              <option value="">공개 여부 선택</option>
              <option value="true">비공개</option>
              <option value="false">공개</option>
            </select>
          </div>
        </div>

        <div className="mt-6 flex items-center space-x-2">
          <div className="flex-grow">
            <p className="mt-5 text-sm text-gray-600">
              총{" "}
              <span className="text-green-700 font-bold">{totalElements}</span>
              건의 프로젝트가 검색되었습니다.
            </p>
          </div>
          <button
            onClick={handleReset}
            className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
            disabled={loading}
          >
            초기화
          </button>
          <button
            onClick={onClose}
            className="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300"
            disabled={loading}
          >
            닫기
          </button>
          <button
            onClick={() => handleSearch(0)}
            className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700"
            disabled={loading}
          >
            {loading ? "검색 중..." : "검색"}
          </button>
        </div>

        {error && (
          <p className="mt-4 text-center text-red-500 font-semibold">{error}</p>
        )}

        {searchResults.length > 0 ? (
          <>
            <div>
              <ProjectTable projects={searchResults} />
            </div>

            <div className="flex justify-center space-x-2 mt-4">
              <button
                disabled={pageNumber === 0 || loading}
                onClick={() => moveToPage(pageNumber - 1)}
                className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
              >
                이전
              </button>

              <span className="px-3 py-1 select-none">
                {pageNumber + 1} / {totalPages}
              </span>

              <button
                disabled={pageNumber + 1 >= totalPages || loading}
                onClick={() => moveToPage(pageNumber + 1)}
                className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
              >
                다음
              </button>
            </div>
          </>
        ) : (
          !loading && (
            <p className="mt-6 text-center text-gray-500">
              검색 결과가 없습니다.
            </p>
          )
        )}
      </div>
    </div>
  );
};

export default ProjectConditionalSearchModal;
