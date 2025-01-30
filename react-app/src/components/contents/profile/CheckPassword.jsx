import React, { useState } from "react";
<<<<<<< HEAD
import { Box, TextField, Button } from "@mui/material";
=======
import { Box, TextField, Button, Snackbar, Alert } from "@mui/material";
import CheckIcon from '@mui/icons-material/Check';
>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00
import axios from "axios";

const CheckPassword = ({
  onCancel,
  onPasswordChecked
}) => {
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
<<<<<<< HEAD
  
=======
  const [openSnackbar, setOpenSnackbar] = useState(false);

>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00
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
<<<<<<< HEAD
=======
      setOpenSnackbar(true);
>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00
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
<<<<<<< HEAD
=======
      {/* 메시지 알림 */}
      <Snackbar
        open={openSnackbar}
        autoHideDuration={3000}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
        onClose={() => setOpenSnackbar(false)}
      >
        <Alert
          onClose={() => setOpenSnackbar(false)}
          severity="error"
          icon={<CheckIcon fontSize="inherit" />}
        >
          비밀번호가 일치하지 않습니다. 다시 입력해주세요.
        </Alert>
      </Snackbar>
>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00
    </Box>
  );
};

export default CheckPassword;