import React, { useState } from "react";
import { Box, TextField, Button } from "@mui/material";
import axios from "axios";

const CheckPassword = ({
  onCancel,
  onPasswordChecked
}) => {
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  const isFormFilled = password.trim();
  
  const handleCheck = async () => {
    setIsLoading(true);
    try {
      const body = {
        password: password
      };

      const result = await axios.post(
        "http://localhost:8080/api/v1/authorization",
        body,
        {
          headers: {
            "Content-Type": "application/json; charset=UTF-8",
          },
          withCredentials: true,
        }
      );

      if (result.status === 200) {
        onPasswordChecked();
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
        label="현재 비밀번호"
        type="password"
        variant="standard"
        margin="normal"
        size="small"
        fullWidth
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <Button
        variant="contained"
        onClick={handleCheck}
        color="primary"
        sx={{ marginTop: "20px", marginBottom: "20px" }}
        disabled={!isFormFilled || isLoading}
        fullWidth
        loading={isLoading}
      >
        확인
      </Button>
      <Button variant="contained" onClick={onCancel} color="inherit" fullWidth>
        취소
      </Button>
    </Box>
  );
};

export default CheckPassword;