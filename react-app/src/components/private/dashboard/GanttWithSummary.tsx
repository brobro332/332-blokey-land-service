import React, { useEffect, useMemo, useRef, useState } from "react";
import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";
import ProgressPieChart from "./ProgressPieChart";
import TaskCountBarChart from "./TaskCountBarChart";
import DelayDonutChart from "./DelayDonutChart";

export interface Task {
  id: number;
  title: string;
  description: string;
  assignee: string | null;
  progress: number;
  status: string;
  priority: string;
  projectId: number | null;
  estimatedStartDate: string;
  estimatedEndDate: string;
  actualStartDate: string | null;
  actualEndDate: string | null;
  start?: string;
  end?: string;
}

interface Project {
  id: number;
  title: string;
}

interface GanttWithSummaryProps {
  tasks: Task[];
  projects: Project[];
}

const MS_PER_DAY = 24 * 60 * 60 * 1000;

const GanttWithSummary: React.FC<GanttWithSummaryProps> = ({
  tasks,
  projects,
}) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const leftRef = useRef<HTMLDivElement>(null);
  const rightRef = useRef<HTMLDivElement>(null);
  const [selectedProjectId, setSelectedProjectId] = useState<number | null>(
    null
  );
  const [hoverTask, setHoverTask] = useState<Task | null>(null);
  const [tooltipPos, setTooltipPos] = useState<{ x: number; y: number } | null>(
    null
  );

  const today = new Date();
  const todayTime = today.getTime();

  const baseStartDate = useMemo(
    () => new Date(todayTime - 15 * MS_PER_DAY),
    [todayTime]
  );
  const daysToShow = 31;
  const dayWidth = 30;
  const rowHeight = 35;
  const headerHeight = 40;
  const minRows = 5;

  useEffect(() => {
    if (projects.length > 0 && selectedProjectId === null) {
      setSelectedProjectId(projects[0].id);
    }
  }, [projects, selectedProjectId]);

  useEffect(() => {
    const right = rightRef.current;
    const left = leftRef.current;
    if (!right || !left) return;

    const syncScroll = () => {
      if (left) left.scrollTop = right.scrollTop;
    };

    right.addEventListener("scroll", syncScroll);
    return () => right.removeEventListener("scroll", syncScroll);
  }, []);

  const todayDiff = Math.floor(
    (today.getTime() - baseStartDate.getTime()) / MS_PER_DAY
  );

  const filteredTasks = useMemo(() => {
    if (!selectedProjectId) return [];
    const chartEndDate = new Date(
      baseStartDate.getTime() + (daysToShow - 1) * MS_PER_DAY
    );

    return tasks
      .filter((task) => task.projectId === selectedProjectId)
      .map((task) => {
        const start = task.actualStartDate ?? task.estimatedStartDate;
        const end = task.actualEndDate ?? task.estimatedEndDate;
        return { ...task, start, end };
      })
      .filter((task) => {
        const taskStart = new Date(task.start!);
        const taskEnd = new Date(task.end!);
        return taskEnd >= baseStartDate && taskStart <= chartEndDate;
      });
  }, [tasks, baseStartDate, daysToShow, selectedProjectId]);

  const progressData = useMemo(() => {
    if (!filteredTasks.length) return [];
    const completed = filteredTasks.filter(
      (t) => (t.progress ?? 0) >= 100
    ).length;
    const inProgress = filteredTasks.filter(
      (t) => (t.progress ?? 0) > 0 && (t.progress ?? 0) < 100
    ).length;
    const notStarted = filteredTasks.filter(
      (t) => (t.progress ?? 0) === 0
    ).length;
    return [
      { name: "완료", value: completed },
      { name: "진행 중", value: inProgress },
      { name: "미완료", value: notStarted },
    ].filter((item) => item.value > 0);
  }, [filteredTasks]);

  const taskCount = filteredTasks.length;
  const visibleRows = Math.max(taskCount, minRows);
  const svgHeight = headerHeight + rowHeight * visibleRows;
  const containerHeight = headerHeight + rowHeight * minRows;
  const enableScroll = taskCount > minRows;
  const delayTasksForDonut = filteredTasks;

  return (
    <div>
      <div className="bg-gray-100 rounded-xl p-4">
        <div className="bg-white flex items-center mb-2 w-64 rounded-xl">
          <Listbox
            value={selectedProjectId}
            onChange={(value) => setSelectedProjectId(value as number)}
          >
            <div className="relative w-full">
              <ListboxButton className="w-full px-4 py-2 border border-gray-300 rounded-lg text-left text-sm focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-500 cursor-pointer transition">
                {projects.find((p) => p.id === selectedProjectId)?.title ??
                  "프로젝트 선택"}
              </ListboxButton>
              <ListboxOptions className="absolute max-h-60 w-full overflow-y-auto bg-white border border-gray-300 rounded-md shadow-lg z-50">
                {projects.map((project) => (
                  <ListboxOption key={project.id} value={project.id}>
                    {({ selected, focus }) => (
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
        </div>

        <div
          ref={containerRef}
          style={{
            position: "relative",
            overflowX: "hidden",
            overflowY: "hidden",
            height: enableScroll ? containerHeight : svgHeight,
            border: "1px solid #ccc",
            borderTopLeftRadius: "12px",
            borderBottomLeftRadius: "12px",
            backgroundColor: "white",
            display: "flex",
            userSelect: "none",
          }}
        >
          <div
            ref={leftRef}
            style={{
              width: 200,
              borderRight: "1px solid rgba(0,0,0,0.1)",
              overflow: "hidden",
              height: "100%",
              boxSizing: "border-box",
              flexShrink: 0,
              position: "relative",
            }}
          >
            <svg width={200} height={svgHeight} style={{ display: "block" }}>
              {filteredTasks.length === 0
                ? Array.from({ length: minRows }).map((_, idx) => (
                    <text
                      key={"empty-" + idx}
                      x={10}
                      y={headerHeight + rowHeight * idx + rowHeight / 2 + 5}
                      fontSize={14}
                      fill="#9CA3AF"
                      textAnchor="middle"
                      style={{ userSelect: "none" }}
                    ></text>
                  ))
                : filteredTasks.map((task, idx) => (
                    <text
                      key={task.id}
                      x={10}
                      y={headerHeight + rowHeight * idx + rowHeight / 2 + 5}
                      fontSize={14}
                      fill="#000"
                    >
                      {task.title}
                    </text>
                  ))}
            </svg>
          </div>

          <div
            ref={rightRef}
            style={{
              flex: 1,
              position: "relative",
              overflowY: enableScroll ? "auto" : "hidden",
              overflowX: "auto",
            }}
          >
            <div
              style={{
                position: "sticky",
                top: 0,
                zIndex: 10,
                backgroundColor: "white",
                height: headerHeight,
                borderBottom: "1px solid rgba(0,0,0,0.1)",
                userSelect: "none",
              }}
            >
              <svg
                width={dayWidth * daysToShow}
                height={headerHeight}
                style={{ display: "block" }}
              >
                {[...Array(daysToShow)].map((_, i) => {
                  const date = new Date(
                    baseStartDate.getTime() + i * MS_PER_DAY
                  );
                  const day = date.getDate();
                  return (
                    <text
                      key={i}
                      x={i * dayWidth + dayWidth / 2}
                      y={20}
                      textAnchor="middle"
                      fontSize={12}
                      fill="#333"
                    >
                      {day}
                    </text>
                  );
                })}
              </svg>
            </div>

            <svg
              width={dayWidth * daysToShow}
              height={svgHeight - headerHeight}
              style={{ display: "block", position: "relative" }}
            >
              {todayDiff >= 0 && todayDiff < daysToShow && (
                <line
                  x1={todayDiff * dayWidth + dayWidth / 2}
                  y1={0}
                  x2={todayDiff * dayWidth + dayWidth / 2}
                  y2={svgHeight - headerHeight}
                  stroke="green"
                  strokeWidth={2}
                  strokeDasharray="4 4"
                />
              )}

              {filteredTasks.length === 0 ? (
                <>
                  <rect
                    x={0}
                    y={0}
                    width={dayWidth * daysToShow}
                    height={svgHeight - headerHeight}
                    fill="#F3F4F6"
                    rx={8}
                    ry={8}
                  />
                  <text
                    x={(dayWidth * daysToShow) / 2}
                    y={(svgHeight - headerHeight) / 2}
                    textAnchor="middle"
                    dominantBaseline="middle"
                    fontSize={18}
                    fill="#9CA3AF"
                    pointerEvents="none"
                    style={{ userSelect: "none" }}
                  >
                    데이터가 없습니다.
                  </text>
                </>
              ) : (
                filteredTasks.map((task, idx) => {
                  const taskStart = new Date(task.start!);
                  const taskEnd = new Date(task.end!);
                  const startOffset = Math.max(
                    0,
                    Math.floor(
                      (taskStart.getTime() - baseStartDate.getTime()) /
                        MS_PER_DAY
                    )
                  );
                  const endOffset = Math.min(
                    daysToShow,
                    Math.floor(
                      (taskEnd.getTime() - baseStartDate.getTime()) / MS_PER_DAY
                    ) + 1
                  );
                  const barX = startOffset * dayWidth;
                  const barWidth = (endOffset - startOffset) * dayWidth;
                  const progressWidth = (task.progress ?? 0) * barWidth * 0.01;

                  return (
                    <g
                      key={task.id}
                      transform={`translate(0, ${rowHeight * idx})`}
                    >
                      <rect
                        x={barX}
                        y={6}
                        width={barWidth}
                        height={rowHeight - 12}
                        fill="#ECECEC"
                        rx={3}
                        ry={3}
                        onMouseEnter={(e) => {
                          setHoverTask(task);
                          setTooltipPos({ x: e.clientX, y: e.clientY });
                        }}
                        onMouseMove={(e) => {
                          setTooltipPos({ x: e.clientX, y: e.clientY });
                        }}
                        onMouseLeave={() => {
                          setHoverTask(null);
                          setTooltipPos(null);
                        }}
                      />
                      <rect
                        x={barX}
                        y={6}
                        width={progressWidth}
                        height={rowHeight - 12}
                        fill="orange"
                        rx={3}
                        ry={3}
                        pointerEvents="none"
                      />
                    </g>
                  );
                })
              )}
            </svg>
          </div>
        </div>
      </div>

      <div
        className="flex items-start w-full"
        style={{ marginTop: 2, maxHeight: 250 }}
      >
        <div className="flex-1 flex justify-center">
          <ProgressPieChart data={progressData} />
        </div>
        <div className="flex-1 flex justify-center">
          <TaskCountBarChart tasks={tasks} projects={projects} />
        </div>
        <div className="flex-1 flex justify-center">
          <DelayDonutChart tasks={delayTasksForDonut} />
        </div>
      </div>

      {hoverTask && tooltipPos && (
        <div
          style={{
            position: "fixed",
            top: tooltipPos.y + 10,
            left: tooltipPos.x + 10,
            backgroundColor: "rgba(0,0,0,0.75)",
            color: "white",
            padding: "6px 10px",
            borderRadius: 4,
            pointerEvents: "none",
            whiteSpace: "nowrap",
            fontSize: 12,
            zIndex: 9999,
            userSelect: "none",
          }}
        >
          <div>
            <strong>{hoverTask.title}</strong>
          </div>
          <div>시작: {hoverTask.start}</div>
          <div>종료: {hoverTask.end}</div>
          <div>진척도: {hoverTask.progress ?? 0}%</div>
        </div>
      )}
    </div>
  );
};

export default GanttWithSummary;
