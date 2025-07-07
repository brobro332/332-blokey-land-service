import React from "react";
import { Droppable, Draggable } from "@hello-pangea/dnd";
import { Task } from "../../../types/task";
import { TaskStatusType } from "../../../types/common";
import TaskPriorityStars from "./TaskPriorityStars";
import TaskProgressBar from "./TaskProgressBar";

const statusLabels: Record<TaskStatusType, string> = {
  TODO: "시작 전",
  IN_PROGRESS: "진행 중",
  REVIEW: "검토 중",
  DONE: "완료",
};

const statusColors: Record<TaskStatusType, string> = {
  TODO: "bg-gray-100 border-gray-300",
  IN_PROGRESS: "bg-blue-200 border-blue-300",
  REVIEW: "bg-yellow-200 border-yellow-300",
  DONE: "bg-green-200 border-green-300",
};

interface TaskColumnProps {
  column: TaskStatusType;
  tasks: Task[];
  onClickAddTask?: () => void;
  onClickTask: (task: Task) => void;
  marginClass?: string;
}

const TaskColumn: React.FC<TaskColumnProps> = ({
  column,
  tasks,
  onClickAddTask,
  onClickTask,
  marginClass = "mx-1",
}) => {
  return (
    <Droppable droppableId={column}>
      {(provided) => (
        <div
          ref={provided.innerRef}
          {...provided.droppableProps}
          className={`flex-1 flex flex-col bg-white rounded-xl shadow p-2 overflow-hidden ${marginClass}`}
        >
          <div className="flex justify-between items-center mb-4 border-b pb-2">
            <h2 className="text-lg font-semibold">{statusLabels[column]}</h2>
            {column === "TODO" && onClickAddTask && (
              <button
                onClick={onClickAddTask}
                className="text-green-600 text-sm font-semibold hover:underline focus:outline-none"
                type="button"
              >
                + 새 태스크 만들기
              </button>
            )}
          </div>

          <div className="flex flex-col space-y-3 overflow-y-auto max-h-[375px] pr-1">
            {tasks
              .sort((a, b) => {
                const aDate = a.actualStartDate
                  ? new Date(a.actualStartDate).getTime()
                  : null;
                const bDate = b.actualStartDate
                  ? new Date(b.actualStartDate).getTime()
                  : null;
                if (aDate && bDate) return bDate - aDate;
                if (aDate) return -1;
                if (bDate) return 1;
                return 0;
              })
              .map((task, index) => (
                <Draggable
                  draggableId={task.id.toString()}
                  index={index}
                  key={task.id}
                >
                  {(provided) => (
                    <div
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                      className={`rounded border p-3 cursor-pointer hover:shadow-md border-gray-300 ${
                        statusColors[task.status]
                      }`}
                      onClick={() => onClickTask(task)}
                    >
                      <div className="flex justify-between items-center mb-1">
                        <h3 className="font-semibold">{task.title}</h3>
                        {task.priority && (
                          <span className="text-xs font-bold px-2 py-0.5 rounded">
                            <TaskPriorityStars priority={task.priority} />
                          </span>
                        )}
                      </div>
                      {!task.actualStartDate &&
                      !task.estimatedStartDate &&
                      !task.actualEndDate &&
                      !task.estimatedEndDate ? (
                        <div className="text-xs text-gray-700 mb-2">미상</div>
                      ) : (
                        <div className="text-xs text-gray-700 mb-2">
                          <span className="text-sm font-semibold text-gray-900">
                            {task.actualStartDate ||
                              task.estimatedStartDate ||
                              "미상"}
                          </span>
                          <span className="mx-1 text-gray-500">~</span>
                          <span className="text-sm font-semibold text-gray-900">
                            {task.actualEndDate ||
                              task.estimatedEndDate ||
                              "미상"}
                          </span>
                        </div>
                      )}
                      {task.progress !== undefined && (
                        <TaskProgressBar progress={task.progress} />
                      )}
                    </div>
                  )}
                </Draggable>
              ))}
            {provided.placeholder}
          </div>
        </div>
      )}
    </Droppable>
  );
};

export default TaskColumn;
