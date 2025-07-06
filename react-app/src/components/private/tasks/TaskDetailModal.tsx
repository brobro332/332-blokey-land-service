import React, { useState } from "react";
import { Task } from "../../../types/task";
import { FaStar } from "react-icons/fa";

interface Props {
  task: Task;
  onClose: () => void;
  onDelete: () => void;
  onUpdate?: (updated: Partial<Task>) => void;
}

const statusLabels = {
  TODO: "시작 전",
  IN_PROGRESS: "진행 중",
  REVIEW: "검토 중",
  DONE: "완료",
};

const priorityLabels = {
  LOW: "낮음",
  MEDIUM: "보통",
  HIGH: "높음",
};

const TaskDetailModal: React.FC<Props> = ({
  task,
  onClose,
  onDelete,
  onUpdate,
}) => {
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState<Partial<Task>>({ ...task });

  const handleSave = () => {
    if (onUpdate) onUpdate(form);
    setEditMode(false);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-xl w-[32rem] shadow-xl space-y-4">
        {editMode ? (
          <div>
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
              className="text-xl font-bold text-gray-800 border p-1 rounded w-full"
              value={form.title ?? ""}
              onChange={(e) => setForm({ ...form, title: e.target.value })}
            />
          </div>
        ) : (
          <h2 className="text-xl font-bold text-gray-800">{task.title}</h2>
        )}

        {editMode ? (
          <div>
            <label className="text-sm font-medium text-gray-700 mb-1 block">
              설명
            </label>
            <textarea
              className="w-full border p-2 rounded text-sm"
              rows={3}
              value={form.description ?? ""}
              onChange={(e) =>
                setForm({ ...form, description: e.target.value })
              }
              placeholder="설명을 입력하세요"
            />
          </div>
        ) : (
          task.description && (
            <p className="text-gray-700 whitespace-pre-line">
              {task.description}
            </p>
          )
        )}

        <div className="text-sm text-gray-600 space-y-1">
          <div className="flex items-start">
            <span className="inline-block min-w-[4.5rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
              진행률
            </span>
            {editMode ? (
              <input
                type="number"
                className="border rounded px-2 py-0.5 text-sm w-full"
                value={form.progress ?? ""}
                onChange={(e) =>
                  setForm({ ...form, progress: Number(e.target.value) })
                }
              />
            ) : (
              <span>{task.progress ?? "미정"}</span>
            )}
          </div>

          <div className="flex items-start">
            <span className="inline-block min-w-[4.5rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
              우선순위
            </span>
            {editMode ? (
              <select
                className="border rounded px-2 py-0.5 text-sm w-full"
                value={form.priority ?? ""}
                onChange={(e) =>
                  setForm({
                    ...form,
                    priority: e.target.value as Task["priority"],
                  })
                }
              >
                {Object.entries(priorityLabels).map(([value, label]) => (
                  <option key={value} value={value}>
                    {label}
                  </option>
                ))}
              </select>
            ) : (
              <span>
                {task.priority
                  ? priorityLabels[task.priority as keyof typeof priorityLabels]
                  : "미정"}
              </span>
            )}
          </div>

          {[
            ["시작예정일", "estimatedStartDate"],
            ["종료예정일", "estimatedEndDate"],
            ["실제시작일", "actualStartDate"],
            ["실제종료일", "actualEndDate"],
          ].map(([label, key]) => (
            <div className="flex items-start" key={key}>
              <span className="inline-block min-w-[4.5rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
                {label}
              </span>
              {editMode ? (
                <input
                  type="date"
                  className="border rounded px-2 py-0.5 text-sm w-full"
                  value={(form as any)[key] ?? ""}
                  onChange={(e) => setForm({ ...form, [key]: e.target.value })}
                />
              ) : (
                <span>{(task as any)[key] ?? "미정"}</span>
              )}
            </div>
          ))}

          <div className="flex items-start">
            <span className="inline-block min-w-[4.5rem] bg-green-100 text-green-700 text-xs font-semibold px-2 py-0.5 mr-2 rounded-full text-center">
              상태
            </span>
            {editMode ? (
              <select
                className="border rounded px-2 py-0.5 text-sm w-full"
                value={form.status}
                onChange={(e) =>
                  setForm({
                    ...form,
                    status: e.target.value as Task["status"],
                  })
                }
              >
                {Object.entries(statusLabels).map(([value, label]) => (
                  <option key={value} value={value}>
                    {label}
                  </option>
                ))}
              </select>
            ) : (
              <span>{statusLabels[task.status]}</span>
            )}
          </div>
        </div>

        <div className="flex justify-end gap-2 pt-4">
          {editMode ? (
            <>
              <button
                onClick={handleSave}
                className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700"
              >
                저장
              </button>
              <button
                onClick={() => {
                  setEditMode(false);
                  setForm({ ...task });
                }}
                className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded"
              >
                취소
              </button>
            </>
          ) : (
            <>
              <button
                onClick={onClose}
                className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded"
              >
                닫기
              </button>
              <button
                onClick={() => {
                  const confirmDelete =
                    window.confirm("정말 삭제하시겠습니까?");
                  if (confirmDelete) onDelete();
                }}
                className="px-4 py-2 rounded bg-red-500 text-white hover:bg-red-600"
              >
                삭제
              </button>
              <button
                onClick={() => setEditMode(true)}
                className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700"
              >
                수정
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default TaskDetailModal;
