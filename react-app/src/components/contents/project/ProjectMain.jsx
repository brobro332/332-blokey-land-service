import React, { useCallback, useEffect, useState } from "react";
import { Box, Divider, FormControl, InputLabel, MenuItem, Select, Typography } from "@mui/material";
import axios from "axios";
import config from "../../../config";
import { Link } from "react-router-dom";

const ProjectMain = () => {
  const [workspaceList, setWorkspaceList] = useState([]);
  const [workspace, setWorkspace] = useState(null);
  const [projectList, setProjectList] = useState([]);
  const [project, setProject] = useState(null);
  const [isCreating, setIsCreating] = useState(false);

  const readWorkspaceList = async () => {
    try {
      const response = await axios.get(`${config.API_BASE_URL}/api/v1/workspace/workspaceList`, {
        withCredentials: true,
      });
      if (response.status === 200) {
        const workspaceList = response.data.data;

        if (workspaceList.length > 0) {
          setWorkspaceList(workspaceList);
          setWorkspace(workspaceList[0]);
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  const readProjectList = useCallback(async () => {
    try {
      const response = await axios.get(`${config.API_BASE_URL}/api/v1/project/projectList`, 
        {
          params: { workspaceId: workspace.id },
          withCredentials: true,
        }
      );
      if (response.status === 200) {
        const projectList = response.data.data;

        if (projectList.length > 0) {
          setProjectList(projectList);
          setProject(projectList[0]);
        }
      }
    } catch (e) {
      console.error(e);
    }
  }, [workspace.id]);

  useEffect(() => {
    readWorkspaceList();
    readProjectList();
  }, [readProjectList]);

  const handleCreateProject = () => {
    setIsCreating(true);
  };

  const handleCancel = () => {
    setIsCreating(false);
  };

  return (
    <Box sx={{ padding: "20px" }}>
      <Typography variant="h6" sx={{ marginBottom : '10px' }}>프로젝트</Typography>
      <Divider sx={{ marginBottom : '15px' }}/>
      {workspaceList.length > 0 && (
        <FormControl sx={{ width: "30%" }}>
          <InputLabel id="select-workspace">워크스페이스 선택</InputLabel>
          <Select
            labelId="select-workspace"
            id="select-workspace"
            size="small"
            value={workspace?.id}
            label="워크스페이스 선택"
            onChange={(e) => {
              const selectedWorkspace = workspaceList.find((workspace) => workspace.id === e.target.value);
              setWorkspace(selectedWorkspace);
            }}
          >
            {workspaceList.map((workspace) => (
              <MenuItem key={workspace.id} value={workspace.id}>
                {workspace.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      )}{" "}
      {workspace != null && projectList.length > 0 ? (
        <FormControl sx={{ width: "30%" }}>
          <InputLabel id="select-project">프로젝트 선택</InputLabel>
          <Select
            labelId="select-project"
            id="select-project"
            size="small"
            value={project?.id}
            label="프로젝트 선택"
            onChange={(e) => {
              const selectedProject = projectList.find((project) => project.id === e.target.value);
              console.log(project.id);
              console.log(e.target.value);
              setProject(selectedProject);
            }}
          >
            {workspaceList.map((project) => (
              <MenuItem key={project.id} value={project.id}>
                {project.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      ) : (
        <Box sx={{ marginTop: '15px' }}>
          생성된 프로젝트가 없습니다.{" "}
          <Link onClick={handleCreateProject} underline="hover">
            프로젝트 생성
          </Link>
        </Box>
      )}{" "}
    </Box>
  );
};

export default ProjectMain;