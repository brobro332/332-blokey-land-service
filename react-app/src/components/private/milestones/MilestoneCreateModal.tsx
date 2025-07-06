import React from "react";
import { Project } from "../../../types/project";
import { FaStar } from "react-icons/fa";
import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";

interface MilestoneCreateModalProps {
  projects: Project[];
  newMilestone: {
    title: string;
    description: string;
    dueDate: string;
    projectId: number | undefined;
  };
  setNewMilestone: React.Dispatch<
    React.SetStateAction<{
      title: string;
      description: string;
      dueDate: string;
      projectId: number | undefined;
    }>
  >;
  selectedDueDate: string;
  selectedProject: Project | undefined;
  onClose: () => void;
  onCreate: (data: {
    title: string;
    description: string;
    dueDate: string;
    projectId: number | undefined;
  }) => void;
}

const MilestoneCreateModal: React.FC<MilestoneCreateModalProps> = ({
  projects,
  newMilestone,
  setNewMilestone,
  selectedDueDate,
  selectedProject,
  onClose,
  onCreate,
}) => {
  return (
    <div
      className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50"
      onClick={onClose}
    >
      <div
        className="bg-white rounded-lg max-w-md w-full p-6 relative"
        onClick={(e) => e.stopPropagation()}
      >
        <h2 className="text-xl font-bold mb-2 border-b pb-3 border-green-400 text-green-700">
          새 마일스톤 만들기
        </h2>

        <label className="inline-flex text-sm font-medium text-gray-700 mb-1">
          프로젝트
          {
            FaStar({
              size: 10,
              className: "text-red-500 ml-1",
            }) as React.ReactElement
          }
        </label>

        {selectedProject ? (
          <div className="block w-full px-3 py-2 border rounded mb-2 text-left bg-gray-100 cursor-not-allowed select-none">
            {selectedProject.title}
          </div>
        ) : (
          <Listbox
            value={newMilestone.projectId ?? ""}
            onChange={(value) =>
              setNewMilestone((prev) => ({
                ...prev,
                projectId: value === "" ? undefined : (value as number),
              }))
            }
          >
            <div className="relative">
              <ListboxButton className="block w-full px-3 py-2 border rounded mb-2 text-left">
                {newMilestone.projectId
                  ? projects.find((p) => p.id === newMilestone.projectId)?.title
                  : "프로젝트 선택"}
              </ListboxButton>

              <ListboxOptions className="absolute z-10 mt-1 max-h-60 w-full overflow-y-auto bg-white border rounded shadow-lg">
                <ListboxOption key="empty" value="">
                  {({ focus, selected }) => (
                    <div
                      className={`cursor-pointer px-4 py-2 ${
                        focus ? "bg-green-100" : ""
                      } ${selected ? "font-semibold" : ""}`}
                    >
                      프로젝트 선택
                    </div>
                  )}
                </ListboxOption>

                {projects.map((project) => (
                  <ListboxOption key={project.id} value={project.id}>
                    {({ focus, selected }) => (
                      <div
                        className={`cursor-pointer px-4 py-2 ${
                          focus ? "bg-green-100" : ""
                        } ${selected ? "font-semibold" : ""}`}
                      >
                        {project.title}
                      </div>
                    )}
                  </ListboxOption>
                ))}
              </ListboxOptions>
            </div>
          </Listbox>
        )}

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
          placeholder="제목"
          value={newMilestone.title}
          onChange={(e) =>
            setNewMilestone((prev) => ({ ...prev, title: e.target.value }))
          }
          className="block w-full px-3 py-2 border rounded mb-2"
        />

        <label className="inline-flex text-sm font-medium text-gray-700 mb-1">
          설명
        </label>
        <textarea
          placeholder="설명"
          value={newMilestone.description}
          onChange={(e) =>
            setNewMilestone((prev) => ({
              ...prev,
              description: e.target.value,
            }))
          }
          className="block w-full px-3 py-2 border rounded mb-2"
        />

        <label className="inline-flex text-sm font-medium text-gray-700 mb-1">
          마감일자
        </label>
        <input
          type="date"
          value={newMilestone.dueDate}
          onChange={(e) =>
            setNewMilestone((prev) => ({
              ...prev,
              dueDate: e.target.value,
            }))
          }
          className="block w-full px-3 py-2 border rounded mb-4"
          min={selectedDueDate}
        />

        <div className="flex justify-end">
          <button
            onClick={onClose}
            className="px-4 py-2 mr-2 bg-gray-300 text-black rounded hover:bg-gray-400"
          >
            취소
          </button>
          <button
            onClick={() => {
              onCreate({
                title: newMilestone.title,
                description: newMilestone.description,
                dueDate: newMilestone.dueDate,
                projectId: newMilestone.projectId,
              });
            }}
            className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700"
          >
            생성
          </button>
        </div>
      </div>
    </div>
  );
};

export default MilestoneCreateModal;
