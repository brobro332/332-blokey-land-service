import React, { useState, useEffect } from "react";
import { DragDropContext, DropResult } from "@hello-pangea/dnd";
import { apiAxios } from "../../../utils/tsx/Api";
import { Task } from "../../../types/task";
import TaskColumn from "./TaskColumn";
import TaskCreateModal from "./TaskCreateModal";
import TaskDetailModal from "./TaskDetailModal";
import { TaskStatusType } from "../../../types/common";
import OfferTableModal from "../offer/OfferTableModal";
import { useLocation, useNavigate } from "react-router-dom";
import { Project } from "../../../types/project";
import MemberTableModal from "../members/MemberTableModal";

const columns: TaskStatusType[] = ["TODO", "IN_PROGRESS", "REVIEW", "DONE"];

const initialForm: Partial<Task> = {
  title: "",
  description: "",
  progress: 0,
  status: "TODO",
  priority: "MEDIUM",
  estimatedStartDate: "",
  estimatedEndDate: "",
  actualStartDate: "",
  actualEndDate: "",
};

const TaskBoard: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [form, setForm] = useState<Partial<Task>>(initialForm);
  const [selectedTask, setSelectedTask] = useState<Task | null>(null);
  const [isOffersModalOpen, setIsOffersModalOpen] = useState(false);
  const [isMemberModalOpen, setIsMemberModalOpen] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();
  const project: Project = location.state?.project;

  /**
   * @description 태스크 생성
   */
  const createTask = async () => {
    if (
      !form.title?.trim() ||
      !form.estimatedStartDate?.trim() ||
      !form.estimatedEndDate?.trim()
    ) {
      alert("제목, 예정 시작일, 예정 종료일은 필수입니다.");
      return;
    }

    try {
      const payload = { ...form, projectId: project.id };

      const response = await apiAxios<Task>("/blokey-land/api/tasks", {
        method: "POST",
        data: payload,
      });

      setTasks((prev) => [...prev, response]);
      setForm(initialForm);
      setIsModalOpen(false);
    } catch (err) {
      console.error("태스크 생성 실패:", err);
      alert("태스크 생성에 실패했습니다.");
    }
  };

  /**
   * @description 태스크 수정
   */
  const updateTask = async (updated: Partial<Task>) => {
    if (!selectedTask) return;
    try {
      await apiAxios(`/blokey-land/api/tasks/${selectedTask.id}`, {
        method: "PATCH",
        data: updated,
      });

      setTasks((prev) =>
        prev.map((t) => (t.id === selectedTask.id ? { ...t, ...updated } : t))
      );
      setSelectedTask((prev) => (prev ? { ...prev, ...updated } : null));
    } catch (err) {
      console.error("수정 실패:", err);
      alert("수정에 실패했습니다.");
    }
  };

  /**
   * @description 태스크 목록 초기화
   */
  useEffect(() => {
    if (project !== null) {
      apiAxios<Task[]>(`/blokey-land/api/projects/${project.id}/tasks/all`, {
        method: "GET",
        withCredentials: true,
      })
        .then((res) => setTasks(res || []))
        .catch(() => setTasks([]));
    } else {
      setTasks([]);
    }
  }, [project]);

  /**
   * @description 태스크 상태 변경
   */
  const updateTaskStatus = async (result: DropResult) => {
    const { source, destination, draggableId } = result;
    if (!destination) return;
    if (destination.droppableId === source.droppableId) return;

    const taskId = parseInt(draggableId);
    const newStatus = destination.droppableId as TaskStatusType;

    try {
      await apiAxios(`/blokey-land/api/tasks/${taskId}`, {
        method: "PATCH",
        data: { status: newStatus },
      });
      setTasks((prev) =>
        prev.map((t) => (t.id === taskId ? { ...t, status: newStatus } : t))
      );
    } catch (err) {
      console.error("상태 변경 실패:", err);
      alert("태스크 상태 변경에 실패했습니다.");
    }
  };

  return (
    <>
      <DragDropContext onDragEnd={updateTaskStatus}>
        <div className="w-full h-full flex flex-col overflow-hidden">
          <div className="px-6 py-4 bg-white flex items-center justify-between flex-shrink-0">
            <div>
              <h2 className="text-xl font-bold text-gray-800 mb-2">
                {project?.title}
              </h2>
              <p className="text-sm text-gray-500">{project?.description}</p>
            </div>

            <div className="flex space-x-2">
              {project?.leader && (
                <>
                  <button
                    onClick={() => setIsMemberModalOpen(true)}
                    className="px-3 py-1 rounded bg-orange-600 text-white hover:bg-orange-700 transition"
                    type="button"
                  >
                    멤버
                  </button>

                  <button
                    onClick={() => setIsOffersModalOpen(true)}
                    className="px-3 py-1 rounded bg-green-600 text-white hover:bg-green-700 transition"
                    type="button"
                  >
                    제안
                  </button>
                </>
              )}

              <button
                onClick={() => {
                  navigate(-1);
                }}
                className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 text-gray-700 transition"
                type="button"
              >
                뒤로가기
              </button>
            </div>
          </div>

          <div className="flex-1 flex overflow-x-auto overflow-y-hidden bg-gray-100 p-4 rounded-xl">
            {columns.map((col, index) => {
              const isFirst = index === 0;
              const isLast = index === columns.length - 1;
              let marginClass = "mx-1";
              if (isFirst) marginClass = "mr-1";
              else if (isLast) marginClass = "ml-1";

              return (
                <TaskColumn
                  key={col}
                  column={col}
                  tasks={tasks.filter((t) => t.status === col)}
                  onClickAddTask={
                    col === "TODO" ? () => setIsModalOpen(true) : undefined
                  }
                  onClickTask={(task) => setSelectedTask(task)}
                  marginClass={marginClass}
                />
              );
            })}
          </div>
        </div>
      </DragDropContext>

      {isModalOpen && (
        <TaskCreateModal
          form={form}
          setForm={setForm}
          onClose={() => setIsModalOpen(false)}
          onSubmit={createTask}
        />
      )}

      {selectedTask && (
        <TaskDetailModal
          task={selectedTask}
          onClose={() => setSelectedTask(null)}
          onDelete={async () => {
            try {
              await apiAxios(`/blokey-land/api/tasks/${selectedTask.id}`, {
                method: "DELETE",
              });
              setTasks((prev) => prev.filter((t) => t.id !== selectedTask.id));
              setSelectedTask(null);
            } catch (err) {
              console.error("삭제 실패:", err);
              alert("삭제에 실패했습니다.");
            }
          }}
          onUpdate={updateTask}
        />
      )}

      {isOffersModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-30">
          <div className="bg-white rounded-xl shadow-lg max-w-4xl w-full max-h-[90vh] overflow-auto p-6">
            <OfferTableModal selectedProjectId={project?.id} />
            <div className="flex justify-end mt-4">
              <button
                onClick={() => setIsOffersModalOpen(false)}
                className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded"
              >
                닫기
              </button>
            </div>
          </div>
        </div>
      )}

      {isMemberModalOpen && project && (
        <MemberTableModal
          projectId={project.id}
          onClose={() => setIsMemberModalOpen(false)}
        />
      )}
    </>
  );
};

export default TaskBoard;
