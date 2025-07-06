import React, { useEffect, useState } from "react";
import { Milestone } from "../../../types/milestone";
import { Project } from "../../../types/project";
import MilestoneProjectListbox from "./MilestoneProjectListbox";
import MilestoneGrid from "./MilestoneGrid";
import MilestoneDeck from "./MilestoneDeck";
import MilestoneCreateModal from "./MilestoneCreateModal";
import * as FaIcons from "react-icons/fa";
import { IconBaseProps } from "react-icons";
import { toLocalDateISO } from "../../../utils/ts/date";

const LeftIcon = FaIcons.FaChevronLeft as React.FC<IconBaseProps>;
const RightIcon = FaIcons.FaChevronRight as React.FC<IconBaseProps>;

interface MilestoneCalendarProps {
  milestones: Milestone[];
  projects: Project[];
  selectedProject: Project | undefined;
  onProjectChange: (project: Project | undefined) => void;
  onCreate: (data: {
    title: string;
    description: string;
    dueDate: string;
    projectId: number;
  }) => Promise<void>;
  refetchMilestones: () => Promise<void>;
}

const MilestoneCalendar: React.FC<MilestoneCalendarProps> = ({
  milestones,
  projects,
  selectedProject,
  onProjectChange,
  onCreate,
  refetchMilestones,
}) => {
  const [currentYear, setCurrentYear] = useState(() =>
    new Date().getFullYear()
  );
  const [currentMonth, setCurrentMonth] = useState(() => new Date().getMonth());
  const [selectedMilestones, setSelectedMilestones] = useState<
    Milestone[] | null
  >(null);
  const [selectedDueDate, setSelectedDueDate] = useState<string | null>(null);
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [newMilestone, setNewMilestone] = useState({
    title: "",
    description: "",
    dueDate: "",
    projectId: undefined as number | undefined,
  });

  const firstDayOfMonth = new Date(currentYear, currentMonth, 1).getDay();
  const lastDateOfMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
  const calendarDays: (number | null)[] = [];
  for (let i = 0; i < firstDayOfMonth; i++) calendarDays.push(null);
  for (let d = 1; d <= lastDateOfMonth; d++) calendarDays.push(d);

  /**
   * @description 마일스톤 생성
   */
  const handleCreate = async () => {
    if (!newMilestone.title.trim() || !newMilestone.projectId) {
      alert("프로젝트와 제목은 필수입니다.");
      return;
    }

    await onCreate({
      title: newMilestone.title,
      description: newMilestone.description,
      dueDate: newMilestone.dueDate,
      projectId: newMilestone.projectId,
    });

    await refetchMilestones();

    setIsCreateModalOpen(false);

    setNewMilestone({
      title: "",
      description: "",
      dueDate: "",
      projectId: undefined,
    });
  };

  /**
   * @description 프로젝트 제목 추출
   */
  const getProjectTitle = (projectId?: number): string => {
    if (!projectId) return "알 수 없음";
    const project = projects.find((p) => p.id === projectId);
    return project?.title ?? "알 수 없음";
  };

  /**
   * @description 날짜 그리드 클릭
   */
  const handleDateClick = (day: number) => {
    if (!day) return;
    const dateStr = toLocalDateISO(currentYear, currentMonth, day);

    const filtered = milestones.filter(
      (ms) => ms.dueDate && ms.dueDate.startsWith(dateStr)
    );

    if (filtered.length > 0) {
      setSelectedMilestones(filtered);
      setIsCreateModalOpen(false);
      setSelectedDueDate(dateStr);
    } else {
      setSelectedDueDate(dateStr);
      setIsCreateModalOpen(true);
      setSelectedMilestones(null);
      setNewMilestone({
        title: "",
        description: "",
        dueDate: dateStr,
        projectId: undefined,
      });
    }
  };

  /**
   * @description 이전달 이동
   */
  const movePrevMonth = () => {
    if (currentMonth === 0) {
      setCurrentYear((y) => y - 1);
      setCurrentMonth(11);
    } else {
      setCurrentMonth((m) => m - 1);
    }
  };

  /**
   * @description 다음달 이동
   */
  const moveNextMonth = () => {
    if (currentMonth === 11) {
      setCurrentYear((y) => y + 1);
      setCurrentMonth(0);
    } else {
      setCurrentMonth((m) => m + 1);
    }
  };

  /**
   * @description 마일스톤 목록 초기화
   */
  useEffect(() => {
    if (!selectedDueDate) return;

    const updatedSelectedMilestones = milestones.filter(
      (ms) => ms.dueDate && ms.dueDate.startsWith(selectedDueDate)
    );

    setSelectedMilestones(
      updatedSelectedMilestones.length > 0 ? updatedSelectedMilestones : null
    );
  }, [milestones, selectedDueDate]);

  return (
    <div
      className="w-full max-w-7xl mx-auto p-2"
      style={{ height: "580px", overflowY: "auto" }}
    >
      {!selectedMilestones && (
        <>
          <div className="relative flex items-center mb-2">
            <div>
              <MilestoneProjectListbox
                projects={projects}
                selectedProject={selectedProject}
                onChange={onProjectChange}
              />
            </div>

            <div className="absolute left-1/2 transform -translate-x-1/2 flex items-center space-x-2">
              <button
                onClick={movePrevMonth}
                aria-label="이전 달"
                className="bg-white border rounded-full p-2 shadow hover:bg-gray-100 flex items-center justify-center"
              >
                <LeftIcon className="w-5 h-5" />
              </button>

              <div className="font-semibold text-lg px-4 select-none">
                {currentYear}년 {currentMonth + 1}월
              </div>

              <button
                onClick={moveNextMonth}
                aria-label="다음 달"
                className="bg-white border rounded-full p-2 shadow hover:bg-gray-100 flex items-center justify-center"
              >
                <RightIcon className="w-5 h-5" />
              </button>
            </div>
          </div>

          <MilestoneGrid
            calendarDays={calendarDays}
            currentYear={currentYear}
            currentMonth={currentMonth}
            milestones={milestones}
            onDateClick={handleDateClick}
          />
        </>
      )}

      {selectedDueDate && selectedMilestones && (
        <MilestoneDeck
          milestones={selectedMilestones}
          getProjectTitle={getProjectTitle}
          onBack={() => {
            setSelectedDueDate(null);
            setSelectedMilestones(null);
          }}
          onCreate={() => {
            if (selectedMilestones[0]?.dueDate) {
              setSelectedDueDate(selectedMilestones[0].dueDate.slice(0, 10));
              setIsCreateModalOpen(true);
              setNewMilestone({
                title: "",
                description: "",
                dueDate: selectedMilestones[0].dueDate.slice(0, 10),
                projectId: undefined,
              });
            }
          }}
          isLeader={selectedProject?.leader}
          displayDate={selectedMilestones[0]?.dueDate?.slice(0, 10) ?? ""}
        />
      )}

      {selectedDueDate && isCreateModalOpen && (
        <MilestoneCreateModal
          projects={projects}
          newMilestone={newMilestone}
          setNewMilestone={setNewMilestone}
          selectedDueDate={selectedDueDate}
          selectedProject={selectedProject}
          onClose={() => {
            setIsCreateModalOpen(false);
          }}
          onCreate={handleCreate}
        />
      )}
    </div>
  );
};

export default MilestoneCalendar;
