interface TaskProgressBarProps {
  progress: number;
}

const TaskProgressBar: React.FC<TaskProgressBarProps> = ({ progress }) => {
  return (
    <div className="flex items-center space-x-2">
      <div className="flex-1 bg-gray-400 rounded-full h-3 overflow-hidden">
        <div
          className="bg-orange-500 h-3 rounded-full transition-all duration-300"
          style={{ width: `${progress}%` }}
        />
      </div>
      <span className="text-xs text-gray-700 w-8 text-right">{progress}%</span>
    </div>
  );
};

export default TaskProgressBar;
