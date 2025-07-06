import React from "react";
import { Milestone } from "../../../types/milestone";
import { toLocalDateISO } from "../../../utils/ts/date";

interface MilestoneGridProps {
  calendarDays: (number | null)[];
  currentYear: number;
  currentMonth: number;
  milestones: Milestone[];
  onDateClick: (day: number) => void;
}

const daysOfWeek = ["일", "월", "화", "수", "목", "금", "토"];

const MilestoneGrid: React.FC<MilestoneGridProps> = ({
  calendarDays,
  currentYear,
  currentMonth,
  milestones,
  onDateClick,
}) => {
  return (
    <>
      <div className="grid grid-cols-7 text-center font-semibold mb-2 min-w-[1000px] overflow-hidden">
        {daysOfWeek.map((day) => (
          <div key={day} className="border p-2 bg-green-100">
            {day}
          </div>
        ))}
      </div>

      <div className="grid grid-cols-7 gap-1 min-w-[1000px]">
        {calendarDays.map((day, idx) => {
          if (day === null)
            return <div key={`empty-${idx}`} className="border h-20"></div>;

          const dateISO = toLocalDateISO(currentYear, currentMonth, day);
          const dayMilestones = milestones.filter(
            (ms) => ms.dueDate && ms.dueDate.startsWith(dateISO)
          );

          return (
            <div
              key={`day-${currentYear}-${currentMonth}-${day}`}
              className="border h-20 p-1 cursor-pointer hover:bg-green-50 relative"
              onClick={() => onDateClick(day)}
            >
              <div className="text-right text-sm font-semibold">{day}</div>
              <ul className="text-xs text-green-700 max-h-14 overflow-y-auto pr-1">
                {dayMilestones.map((ms) => (
                  <li
                    key={`${ms.id ?? `${ms.title}-${ms.dueDate}`}-${dateISO}`}
                    className="truncate mb-1 relative"
                  >
                    • {ms.title}
                  </li>
                ))}
              </ul>
            </div>
          );
        })}
      </div>
    </>
  );
};

export default MilestoneGrid;
