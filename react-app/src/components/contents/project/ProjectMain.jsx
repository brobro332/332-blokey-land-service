import React, { useEffect, useState } from "react";
import { Box, Divider, Typography } from "@mui/material";
import axios from "axios";
import config from "../../../config";

const ProjectMain = () => {
  const [workspaceList, setWorkspaceList] = useState([]);
  const [selectedWorkspace, setSelectedWorkspace] = useState(null);

  const readWorkspaceList = async () => {
    try {
      const response = await axios.get(`${config.API_BASE_URL}/api/v1/workspace/workspaceList`, {
        withCredentials: true,
      });
      if (response.status === 200) {
        const workspaceList = response.data.data;

        if (workspaceList.length > 0) {
          setWorkspaceList(workspaceList);
          setSelectedWorkspace(workspaceList[0]);
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    readWorkspaceList();
  }, []);  

  return (
    <Box sx={{ padding: "20px" }}>
      <Typography variant="h6" sx={{ marginBottom : '10px' }}>프로젝트</Typography>
      <Divider sx={{ marginBottom : '15px' }}/>
    </Box>
  );
};

export default ProjectMain;