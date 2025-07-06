import React from "react";
import { PriorityType } from "../../../types/common";

interface TaskPriorityStarsProps {
  priority: PriorityType;
}

const priorityStars: Record<PriorityType, number> = {
  LOW: 1,
  MEDIUM: 2,
  HIGH: 3,
};

const TaskPriorityStars: React.FC<TaskPriorityStarsProps> = ({ priority }) => {
  const starCount = priorityStars[priority];

  return (
    <div className="flex space-x-1 text-yellow-500">
      {Array.from({ length: starCount }).map((_, idx) => (
        <svg
          key={idx}
          className="w-4 h-4 fill-current"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 20 20"
        >
          <path d="M10 15l-5.878 3.09 1.123-6.545L.49 6.91l6.562-.955L10 0l2.948 5.955 6.562.955-4.755 4.635 1.123 6.545z" />
        </svg>
      ))}
    </div>
  );
};

export default TaskPriorityStars;
