import React, { useEffect, useState } from 'react';
import { Box, Typography, Divider, Card, Chip, Button } from "@mui/material";
import axios from "axios";
import EditProfile from "./EditProfile";
import CheckPassword from "./CheckPassword";
import UpdatePassword from "./UpdatePassword";

const Profile = () => {
  const [member, setMember] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isChecking, setIsChecking] = useState(false);
  const [isUpdatingPassword, setIsUpdatingPassword] = useState(false);

  const fetchMember = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/v1/member", {
        withCredentials: true,
      });
      if (response.status === 200) {
        const member = response.data.data;

        if (member !== null) {
          setMember(member);
        }
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleProfileEdited = () => {
    setIsEditing(false);
    fetchMember();
  };

  const handleCancel = () => {
    setIsEditing(false);
    setIsChecking(false);
    setIsUpdatingPassword(false);
  };

  const handleCheckPassword = () => {
    setIsChecking(false);
    setIsUpdatingPassword(true);
  };

  const handleUpdatePassword = () => {
    setIsUpdatingPassword(false);
  };

  const formatDate = (date) => {
    const newDate = new Date(date);
    return newDate.toLocaleDateString("en-CA");
  };

  useEffect(() => {
    fetchMember();
  }, []);

  return (
    <Box sx={{ padding: "20px" }}>
      <Typography variant="h6" sx={{ marginBottom: '10px' }}>프로필</Typography>
      <Divider sx={{ marginBottom: '15px' }} />
      {isEditing ? (
        <>
          <EditProfile 
            member={member} 
            onCancel={handleCancel} 
            onProfileEdited={handleProfileEdited} 
          />
        </>
      ) : (
        <>
          {isChecking ? (
            <CheckPassword
              onCancel={handleCancel}
              onPasswordChecked={handleCheckPassword}
            />
          ) : (
            <>
              {isUpdatingPassword ? (
                <>
                  <UpdatePassword 
                    onCancel={handleCancel}
                    onPasswordUpdated={handleUpdatePassword}
                  />
                </>
              ) : (
                <>
                  <Button variant="contained" color="primary" onClick={()=>{setIsEditing(true);}}>
                    정보 편집
                  </Button>
                  <Button
                    onClick={()=>{setIsChecking(true);}}
                    variant="contained"
                    color="primary"
                    sx={{ marginLeft: "10px" }}
                  >
                    패스워드 변경
                  </Button>
                  <Button
                    variant="contained"
                    color="error"
                    sx={{ marginLeft: "10px" }}
                  >
                    회원 탈퇴
                  </Button>
                  <Card variant="outlined" sx={{ padding: 2, marginTop: 2, width: '50%' }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: 2 }}>
                      <Box sx={{ flex: 1, marginRight: 2 }}>
                        <Chip label="아이디" color="primary" variant="outlined" />
                      </Box>
                      <Typography variant="body2" sx={{ flex: 5 }}>
                        {member?.email}
                      </Typography>
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: 2 }}>
                      <Box sx={{ flex: 1, marginRight: 2 }}>
                        <Chip label="이름" color="primary" variant="outlined" />
                      </Box>
                      <Typography variant="body2" sx={{ flex: 5 }}>
                        {member?.name}
                      </Typography>
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: 2 }}>
                      <Box sx={{ flex: 1, marginRight: 2 }}>
                        <Chip label="소개" color="primary" variant="outlined" />
                      </Box>
                      <Typography variant="body2" sx={{ flex: 5 }}>
                        {member?.description}
                      </Typography>
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', marginBottom: 2 }}>
                      <Box sx={{ flex: 1, marginRight: 2 }}>
                        <Chip label="생성일자" color="primary" variant="outlined" />
                      </Box>
                      <Typography variant="body2" sx={{ flex: 5 }}>
                        {member?.createdAt ? formatDate(member.createdAt) : '-'}
                      </Typography>
                    </Box>
                  </Card>
                </>
              )}
            </>
          )}
        </>
      )}
    </Box>
  );
};

export default Profile;