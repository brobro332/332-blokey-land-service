import React from "react";
import { Project } from "../../../types/project";
import {
  Listbox,
  ListboxButton,
  ListboxOption,
  ListboxOptions,
} from "@headlessui/react";

interface MilestoneProjectListboxProps {
  projects: Project[];
  selectedProject: Project | undefined;
  onChange: (selectedProject: Project | undefined) => void;
}

const MilestoneProjectListbox: React.FC<MilestoneProjectListboxProps> = ({
  projects,
  selectedProject,
  onChange,
}) => {
  return (
    <div className="flex items-center mb-2 space-x-4 w-64">
      <Listbox
        value={selectedProject ?? null}
        onChange={(value) =>
          onChange(value === null ? undefined : (value as Project))
        }
      >
        <div className="relative w-full">
          <ListboxButton
            className="
              w-full
              px-4 py-2
              border border-gray-300 rounded-lg
              text-left text-sm
              focus:outline-none focus:ring-2 focus:ring-green-400 focus:border-green-500
              cursor-pointer
              transition
            "
          >
            {selectedProject ? selectedProject.title : "프로젝트 선택 (전체)"}
          </ListboxButton>

          <ListboxOptions
            className="
              absolute mt-1 max-h-60 w-full
              overflow-y-auto
              bg-white border border-gray-300
              rounded-md shadow-lg
              z-10
            "
          >
            <ListboxOption value={null}>
              {({ selected, focus }) => (
                <div
                  className={`cursor-pointer px-4 py-2 ${
                    focus ? "bg-green-100" : ""
                  } ${selected ? "font-semibold" : ""}`}
                >
                  프로젝트 선택 (전체)
                </div>
              )}
            </ListboxOption>

            {projects.map((project) => (
              <ListboxOption key={project.id} value={project}>
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
  );
};

export default MilestoneProjectListbox;
