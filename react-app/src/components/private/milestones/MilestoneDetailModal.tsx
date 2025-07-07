import { useEffect, useState } from "react";
import { Milestone } from "../../../types/milestone";
import { FaStar } from "react-icons/fa";

const MilestoneDetailModal: React.FC<{
  milestone: Milestone;
  projectTitle: string;
  onClose: () => void;
  onUpdate: (updated: Milestone) => void;
  onDelete: (id: number) => void;
  isLeader: boolean | undefined;
}> = ({ milestone, projectTitle, onClose, onUpdate, onDelete, isLeader }) => {
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState<Milestone>({ ...milestone });

  /**
   * @description 마일스톤 저장
   */
  const handleSaveClick = () => {
    if (!form.title.trim()) {
      alert("제목은 필수입니다.");
      return;
    }
    onUpdate(form);
    setEditMode(false);
  };

  /**
   * @description 마일스톤 삭제
   */
  const handleDeleteClick = () => {
    if (window.confirm("정말 삭제하시겠습니까?")) {
      onDelete(milestone.id!);
      onClose();
    }
  };

  /**
   * @description 상세보기 폼 초기화
   */
  useEffect(() => {
    setEditMode(false);
    setForm({ ...milestone });
  }, [milestone]);

  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      onClick={onClose}
    >
      <div
        className="bg-white rounded-lg p-6 max-w-md w-full relative"
        onClick={(e) => e.stopPropagation()}
      >
        <div>
          {editMode ? (
            <>
              <label className="inline-flex text-sm font-medium text-gray-700 mb-1">
                제목
                {
                  FaStar({
                    size: 10,
                    className: "text-red-500 ml-1",
                  }) as React.ReactElement
                }
              </label>
              <input
                type="text"
                className="w-full border rounded px-3 py-2 mb-2"
                value={form.title}
                onChange={(e) => setForm({ ...form, title: e.target.value })}
              />
            </>
          ) : (
            <h2 className="text-xl font-bold text-gray-800">{form.title}</h2>
          )}
        </div>

        <div>
          {editMode ? (
            <>
              <label className="inline-flex text-sm font-medium text-gray-700 mb-1">
                설명
              </label>
              <textarea
                className="w-full border rounded px-3 py-2"
                rows={4}
                value={form.description || ""}
                onChange={(e) =>
                  setForm({ ...form, description: e.target.value })
                }
              />
            </>
          ) : (
            form.description && (
              <p className="text-gray-700 whitespace-pre-line">
                {form.description}
              </p>
            )
          )}
        </div>

        <div>
          {editMode ? (
            <>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                프로젝트
              </label>
              {projectTitle}
            </>
          ) : (
            <>
              <span className="inline-block min-w-[4.5rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
                프로젝트
              </span>
              {projectTitle}
            </>
          )}
        </div>

        <div>
          {editMode ? (
            <>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                마감일자
              </label>
              {form.dueDate || "미정"}
            </>
          ) : (
            <>
              <span className="inline-block min-w-[4.5rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
                마감일자
              </span>
              {form.dueDate || "미정"}
            </>
          )}
        </div>

        <div className="flex justify-end space-x-2">
          {editMode ? (
            <>
              <button
                onClick={() => {
                  setEditMode(false);
                  setForm({ ...milestone });
                }}
                className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
              >
                취소
              </button>
              <button
                onClick={handleSaveClick}
                className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
              >
                저장
              </button>
            </>
          ) : (
            <>
              <button
                onClick={onClose}
                className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
              >
                닫기
              </button>
              {isLeader && (
                <>
                  <button
                    onClick={handleDeleteClick}
                    className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                  >
                    삭제
                  </button>
                  <button
                    onClick={() => setEditMode(true)}
                    className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
                  >
                    수정
                  </button>
                </>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default MilestoneDetailModal;
