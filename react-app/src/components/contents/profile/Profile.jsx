import React, { useEffect, useState } from 'react';
import { Box, Typography, Divider, Card, Chip, Button } from "@mui/material";
import axios from "axios";
import EditProfile from "./EditProfile";
import CheckPassword from "./CheckPassword";
import UpdatePassword from "./UpdatePassword";
<<<<<<< HEAD
=======
import ConfirmDialog from '../../tags/ConfirmDialog';
import { useNavigate } from 'react-router-dom';
>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00

const Profile = () => {
  const [member, setMember] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [isChecking, setIsChecking] = useState(false);
  const [isUpdatingPassword, setIsUpdatingPassword] = useState(false);
<<<<<<< HEAD
=======
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const navigate = useNavigate();
>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00

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

<<<<<<< HEAD
=======
  const deleteMember = async () => {
    try {
      const resultObject = await axios.delete(
        "http://localhost:8080/api/v1/member",
        {
          withCredentials: true
        }
      );
      if (resultObject.status === 200) {
        navigate('/');
      }
    } catch (e) {
      console.error(e);
    } 
  };

>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00
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

<<<<<<< HEAD
=======
  const handleMemberDeleted = () => {
    deleteMember();
  };

  const handleCancelDelete = () => {
    setIsDialogOpen(false);
  };

>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00
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
<<<<<<< HEAD
=======
                    onClick={()=>{setIsDialogOpen(true);}}
>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00
                    color="error"
                    sx={{ marginLeft: "10px" }}
                  >
                    회원 탈퇴
                  </Button>
<<<<<<< HEAD
=======
                  <ConfirmDialog 
                    open={isDialogOpen}
                    onConfirm={handleMemberDeleted}
                    onClose={handleCancelDelete}
                    title={'회원 탈퇴'} 
                    content={'정말 회원 탈퇴하시겠습니까?\n30일 안에 재접속을 통해 복구 가능하며, 이후에 회원정보가 삭제됩니다.'}
                  />
>>>>>>> 95a32e98b5742ff03b2b86db8ebc6fd36b33cd00
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