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
  items,
  onCreateWorkspace,
  onEditWorkspace,
  onAddMember,
  handleDeleteButtonClick,
  selectedItem,
  setSelectedItem
}) => (
  <Box>
    {items.length > 0 ? (
      <div>
        <FormControl sx={{ width: "30%" }}>
          <InputLabel id="select-workspace">워크스페이스 선택</InputLabel>
          <Select
            labelId="select-workspace"
            id="select-workspace"
            size="small"
            value={selectedItem.id}
            label="워크스페이스 선택"
            onChange={(e) => {
              const selected = items.find((item) => item.id === e.target.value);
              setSelectedItem(selected);
            }}
          >
            {items.map((item) => (
              <MenuItem key={item.id} value={item.id}>
                {item.name}
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