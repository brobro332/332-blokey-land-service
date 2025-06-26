import React, { useState } from "react";
import { FaStar } from "react-icons/fa";

interface ProjectModalProps {
  isOpen: boolean;
  onClose: () => void;
  onCreate: (
    title: string,
    description: string,
    estimatedStartDate: string,
    estimatedEndDate: string,
    actualStartDate: string,
    actualEndDate: string
  ) => void;
}

const ProjectModal: React.FC<ProjectModalProps> = ({
  isOpen,
  onClose,
  onCreate,
}) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [estimatedStartDate, setEstimatedStartDate] = useState("");
  const [estimatedEndDate, setEstimatedEndDate] = useState("");
  const [actualStartDate, setActualStartDate] = useState("");
  const [actualEndDate, setActualEndDate] = useState("");

  if (!isOpen) return null;

  const handleSubmit = () => {
    if (title.trim() === "") {
      alert("프로젝트 제목을 입력해주세요.");
      return;
    }
    onCreate(
      title,
      description,
      estimatedStartDate,
      estimatedEndDate,
      actualStartDate,
      actualEndDate
    );
    setTitle("");
    setDescription("");
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-96 shadow-lg">
        <h2 className="text-xl font-bold mb-4">새 프로젝트 만들기</h2>
        <label className="inline-flex text-sm font-medium mb-1">
          프로젝트 제목
          {
            FaStar({
              size: 10,
              className: "text-red-500",
            }) as React.ReactElement
          }
        </label>
        <input
          type="text"
          placeholder="프로젝트 제목"
          className="w-full border border-gray-300 rounded px-3 py-2 mb-3 focus:outline-none focus:ring-2 focus:ring-green-400"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />
        <label className="block text-sm font-medium mb-1">프로젝트 설명</label>
        <textarea
          placeholder="프로젝트 설명"
          className="w-full border border-gray-300 rounded px-3 py-2 mb-1 focus:outline-none focus:ring-2 focus:ring-green-400 resize-none"
          rows={4}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <div className="mb-3">
          <label className="block text-sm font-medium mb-1">예상 시작일</label>
          <input
            type="date"
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
            value={estimatedStartDate}
            onChange={(e) => setEstimatedStartDate(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label className="block text-sm font-medium mb-1">예상 종료일</label>
          <input
            type="date"
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
            value={estimatedEndDate}
            onChange={(e) => setEstimatedEndDate(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label className="block text-sm font-medium mb-1">실제 시작일</label>
          <input
            type="date"
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
            value={actualStartDate}
            onChange={(e) => setActualStartDate(e.target.value)}
          />
        </div>

        <div className="mb-3">
          <label className="block text-sm font-medium mb-1">실제 종료일</label>
          <input
            type="date"
            className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
            value={actualEndDate}
            onChange={(e) => setActualEndDate(e.target.value)}
          />
        </div>
        <div className="flex justify-end space-x-2">
          <button
            className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
            onClick={() => {
              setTitle("");
              setDescription("");
              onClose();
            }}
          >
            취소
          </button>
          <button
            className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700"
            onClick={handleSubmit}
          >
            생성
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProjectModal;
