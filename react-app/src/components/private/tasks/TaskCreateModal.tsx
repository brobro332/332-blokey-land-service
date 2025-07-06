import React from "react";
import { Task } from "../../../types/task";
import { PriorityType } from "../../../types/common";
import { FaStar } from "react-icons/fa";

interface Props {
  form: Partial<Task>;
  setForm: React.Dispatch<React.SetStateAction<Partial<Task>>>;
  onClose: () => void;
  onSubmit: () => void;
}

const TaskCreateModal: React.FC<Props> = ({
  form,
  setForm,
  onClose,
  onSubmit,
}) => {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-2xl shadow-xl w-[32rem]">
        <h2 className="text-xl font-bold mb-6 border-b pb-3 border-green-400 text-green-700">
          새 태스크 만들기
        </h2>

        <div className="space-y-4">
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
              className="w-full border rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-green-400"
              placeholder="제목을 입력하세요"
              value={form.title}
              onChange={(e) => setForm({ ...form, title: e.target.value })}
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              설명
            </label>
            <textarea
              className="w-full border rounded-md p-2 resize-none h-24 focus:outline-none focus:ring-2 focus:ring-green-400"
              placeholder="설명을 입력하세요"
              value={form.description}
              onChange={(e) =>
                setForm({ ...form, description: e.target.value })
              }
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="inline-flex text-sm font-medium text-gray-700 mb-1">
                시작 예정일
                {
                  FaStar({
                    size: 10,
                    className: "text-red-500 ml-1",
                  }) as React.ReactElement
                }
              </label>
              <input
                className="w-full border rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-green-400"
                type="date"
                value={form.estimatedStartDate}
                onChange={(e) =>
                  setForm({ ...form, estimatedStartDate: e.target.value })
                }
              />
            </div>
            <div>
              <label className="inline-flex text-sm font-medium text-gray-700 mb-1">
                종료 예정일
                {
                  FaStar({
                    size: 10,
                    className: "text-red-500 ml-1",
                  }) as React.ReactElement
                }
              </label>
              <input
                className="w-full border rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-green-400"
                type="date"
                value={form.estimatedEndDate}
                onChange={(e) =>
                  setForm({ ...form, estimatedEndDate: e.target.value })
                }
              />
            </div>
          </div>

          <div>
            <label className="inline-flex text-sm font-medium text-gray-700 mb-1">
              우선순위
              {
                FaStar({
                  size: 10,
                  className: "text-red-500 ml-1",
                }) as React.ReactElement
              }
            </label>
            <select
              className="w-full border rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-green-400"
              value={form.priority}
              onChange={(e) =>
                setForm({ ...form, priority: e.target.value as PriorityType })
              }
            >
              <option value="LOW">낮음</option>
              <option value="MEDIUM">중간</option>
              <option value="HIGH">높음</option>
            </select>
          </div>
        </div>

        <div className="flex justify-end gap-3 mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 rounded-md bg-gray-100 hover:bg-gray-200 text-gray-700"
          >
            취소
          </button>
          <button
            onClick={onSubmit}
            className="px-4 py-2 rounded-md bg-green-600 text-white hover:bg-green-700"
          >
            생성
          </button>
        </div>
      </div>
    </div>
  );
};

export default TaskCreateModal;
