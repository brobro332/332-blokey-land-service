import React, { useCallback, useEffect, useState } from "react";
import { Box, Card, Chip, Divider, Typography } from "@mui/material";
import axios from "axios";
import config from "../../../config";

const ProjectMain = () => {

  return (
    <Box sx={{ padding: "20px" }}>
      <Typography variant="h6" sx={{ marginBottom : '10px' }}>프로젝트</Typography>
      <Divider sx={{ marginBottom : '15px' }}/>
    </Box>
  );
};

export default ProjectMain;