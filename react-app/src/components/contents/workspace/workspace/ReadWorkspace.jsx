import React from "react";
import {
  Box,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
  Link
} from "@mui/material";

const ReadWorkspace = ({
  workspaceList,
  onCreateWorkspace,
  onEditWorkspace,
  onAddMember,
  handleDeleteButtonClick,
  workspace,
  setWorkspace
}) => (
  <Box>
    {workspaceList.length > 0 ? (
      <div>
        <FormControl sx={{ width: "30%" }}>
          <InputLabel id="select-workspace">워크스페이스 선택</InputLabel>
          <Select
            labelId="select-workspace"
            id="select-workspace"
            size="small"
            value={workspace.id}
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
        </FormControl>{" "}
        <Button variant="contained" color="primary" onClick={onCreateWorkspace}>
          워크스페이스 생성
        </Button>
        <Button
          variant="contained"
          onClick={onEditWorkspace}
          color="primary"
          sx={{ marginLeft: "10px" }}
        >
          워크스페이스 편집
        </Button>
        <Button 
          variant="contained" 
          onClick={onAddMember}
          color="primary" 
          sx={{ marginLeft: "10px" }}
        >
          가입관리
        </Button>
        <Button
          variant="contained"
          onClick={handleDeleteButtonClick}
          color="error"
          sx={{ marginLeft: "10px" }}
        >
          워크스페이스 삭제
        </Button>
      </div>
    ) : (
      <Box>
        생성된 워크스페이스가 없습니다.{" "}
        <Link onClick={onCreateWorkspace} underline="hover">
          워크스페이스 생성
        </Link>
      </Box>
    )}
  </Box>
);

export default ReadWorkspace;