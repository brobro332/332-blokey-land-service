import React, { useState } from "react";
import { Box, TextField, Button } from "@mui/material";
import axios from "axios";

const EditProfile = ({
  member,
  onCancel,
  onProfileEdited
}) => {
  const [name, setName] = useState(member?.name ? member.name : '');
  const [description, setDescription] = useState(member?.description ? member.description : '');
  const [isLoading, setIsLoading] = useState(false);

  const isFormFilled = name.trim() && description.trim();

  const handleEdit = async () => {
    setIsLoading(true);
    try {
      const body = {
        name: name,
        description: description,
      };

      const result = await axios.put(
        "http://localhost:8080/api/v1/member",
        body,
        {
          headers: {
            "Content-Type": "application/json; charset=UTF-8",
          },
          withCredentials: true,
        }
      );

      if (result.status === 200) {
        onProfileEdited();
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
        label="이름"
        variant="standard"
        margin="normal"
        size="small"
        fullWidth
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <TextField
        label="소개"
        variant="standard"
        margin="normal"
        size="small"
        fullWidth
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
      <Button
        variant="contained"
        onClick={handleEdit}
        color="primary"
        sx={{ marginTop: "20px", marginBottom: "20px" }}
        disabled={!isFormFilled || isLoading}
        fullWidth
        loading={isLoading}
      >
        저장
      </Button>
      <Button variant="contained" onClick={onCancel} color="inherit" fullWidth>
        취소
      </Button>
    </Box>
  );
};

export default EditProfile;