import React, { useState } from "react";
import { Tabs, Tab, Box, Button } from "@mui/material";
import ApproveRequest from "./ApproveRequest";
import CreateRequest from "./CreateRequest";

const ManageRequest = ({ onCancel }) => {
  const [value, setValue] = useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <Box>
      <Tabs 
        value={value} 
        onChange={handleChange} 
        centered 
        variant="scrollable" 
        scrollButtons="auto"
      >
        <Tab label="가입요청" />
        <Tab label="신청관리" />
      </Tabs>
      
      <Box sx={{ p: 2, mt: 2, border: "1px solid #ddd", borderRadius: 2 }}>
        {value === 0 && (
          <ApproveRequest/>
        )}
        {value === 1 && (
          <CreateRequest/>
        )}
      </Box>
      <Button variant="contained" color="inherit" onClick={onCancel} sx={{marginTop : "15px"}}>
        뒤로가기
      </Button>
    </Box> 
  );
};

export default ManageRequest;