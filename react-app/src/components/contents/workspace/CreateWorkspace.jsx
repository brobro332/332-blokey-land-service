import React, { useEffect, useState } from "react";
import { Box, TextField, Button } from "@mui/material";
import { useUser } from "../../hooks/UserProvider";
import axios from "axios";

const CreateWorkspace = ({
  onCancel,
  onWorkspaceCreated,
  onWorkspaceUpdated,
  selectedItem,
  isEditing,
}) => {
  const [name, setName] = useState(selectedItem ? selectedItem.name : "");
  const [description, setDescription] = useState(
    selectedItem ? selectedItem.description : ""
  );
  const [isLoading, setIsLoading] = useState(false);

  const { user } = useUser();
  const isFormFilled = name.trim() && description.trim();

  useEffect(() => {
    if (!isEditing) {
      setName('');
      setDescription('');
    }
  }, [isEditing]);

  const handleCreate = async () => {
    setIsLoading(true);
    try {
      const body = {
        email: user.email,
        name: name,
        description: description,
      };

      const result = await axios.post(
        "http://localhost:8080/api/v1/team",
        body,
        {
          headers: {
            "Content-Type": "application/json; charset=UTF-8",
          },
          withCredentials: true,
        }
      );

      if (result.status === 200) {
        onWorkspaceCreated(body);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setIsLoading(false);
    }
  };

  const handleUpdate = async () => {
    setIsLoading(true);
    try {
      const body = {
        id: selectedItem.id,
        email: user.email,
        name: name,
        description: description,
      };

      const result = await axios.put(
        "http://localhost:8080/api/v1/team/" + selectedItem.id,
        body,
        {
          headers: {
            "Content-Type": "application/json; charset=UTF-8",
          },
          withCredentials: true,
        }
      );

      if (result.status === 200) {
        onWorkspaceUpdated(body);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box>
      <TextField
        label="워크스페이스 이름"
        variant="standard"
        margin="normal"
        size="small"
        fullWidth
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <TextField
        label="워크스페이스 설명"
        variant="standard"
        margin="normal"
        size="small"
        fullWidth
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
      <Button
        variant="contained"
        onClick={isEditing ? handleUpdate : handleCreate}
        color="primary"
        sx={{ marginTop: "20px", marginBottom: "20px" }}
        disabled={!isFormFilled || isLoading}
        fullWidth
        loading={isLoading}
      >
        {isEditing
          ? "수정"
          : "생성"}
      </Button>
      <Button variant="contained" onClick={onCancel} color="inherit" fullWidth>
        취소
      </Button>
    </Box>
  );
};

export default CreateWorkspace;