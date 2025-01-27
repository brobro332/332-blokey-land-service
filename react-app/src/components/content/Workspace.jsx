import React, { useEffect, useState } from 'react';
import { Typography, Select, MenuItem, FormControl, InputLabel, Box, TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Paper, Link, TextField, Button } from '@mui/material';
import axios from 'axios';
import { useUser } from '../hook/UserProvider';

const Workspace = () => {
  const [items, setItems] = useState([]);
  const [selectedItem, setSelectedItem] = useState('');
  const [teamMemberList, setTeamMemberList] = useState([]);
  const [isCreating, setIsCreating] = useState(false);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const { user } = useUser();

  const isFormFilled =
    name.trim() &&
    description.trim();

  useEffect(() => {
    const handleOnload = async () => {
      try {
        const result1 = await axios.get(
          "http://localhost:8080/api/v1/team",
          { 
            params: { email: user.email },
            withCredentials: true
          }
        );    
        if (result1.status === 200) { 
          setItems(result1.data.data);
          if (result1.data.data.length > 0) {
            setSelectedItem(result1.data.data[0].name);
            const result2 = await axios.get(
              "http://localhost:8080/api/v1/team/member",
              { 
                params: { id: selectedItem.id },
                withCredentials: true
              }
            );
            if (result2.status === 200) { 
              setTeamMemberList(result2.data.data);
            }
          }
        }
      } catch (e) {
        console.error(e);
      }
    };

    handleOnload();
  }, []);

  const handleCreateWorkspaceButton = () => {
    setIsCreating(true);
  };

  const handleCreate = async () => {
    try {
      const result = await axios.post(
        "http://localhost:8080/api/v1/team",
        { 
          email: user.email,
          name: name,
          description: description
        },
        { 
          headers: {
            'Content-Type': 'application/json; charset=UTF-8',
          }, 
          withCredentials: true
        }
      );
  
      if (result.status === 200) {
        console.log(result);
      } 
    } catch (e) {
      console.error(e)
    }
  };

  const handleCancel = () => {
    setIsCreating(false);
  };

  return (
    <div>
      <Typography variant="h5" sx={{ marginBottom: '20px' }}>
        워크스페이스
      </Typography>
      <Box>
        {isCreating ? (
          <Box>
            {/* 입력 필드 */}
            <TextField
              label='워크스페이스 이름'
              variant='standard'
              margin='normal'
              size='small'
              fullWidth
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
            <TextField
              label='워크스페이스 소개'
              variant='standard'
              margin='normal'
              size='small'
              fullWidth
              value={description}
              onChange={(e) => setDescription(e.target.value)}
            />
            <Button
              variant='contained'
              onClick={handleCreate}
              color='primary'
              sx={{ marginTop: '20px', marginBottom: '20px' }}
              disabled={!isFormFilled}
              fullWidth
              loading={isLoading}
            >
              생성
            </Button>
            <Button
              variant='contained'
              onClick={handleCancel}
              color='inherit'
              fullWidth
            >
              취소
            </Button>
          </Box>
        ) : (
          <>
            {items.length > 0 ? (
              <FormControl fullWidth>
                <InputLabel id="select-workspace">워크스페이스 선택</InputLabel>
                <Select
                  labelId="select-workspace"
                  id="select-workspace"
                  value={selectedItem}
                  label="워크스페이스 선택"
                  onChange={(e) => setSelectedItem(e.target.value)}
                >
                  {items.map((item) => (
                    <MenuItem key={item.id} value={item.name}>
                      {item.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            ) : (
              <Box>
                생성된 워크스페이스가 없습니다.{' '}
                <Link onClick={handleCreateWorkspaceButton} underline="hover">
                  워크스페이스 생성
                </Link>
              </Box>
            )}
          </>
        )}
      </Box>

      <Box sx={{ marginTop: '20px' }}>
        {items.length > 0 && (
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>팀원명</TableCell>
                  <TableCell>이메일</TableCell>
                  <TableCell>소개</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {teamMemberList.map((member) => (
                  <TableRow key={member.id}>
                    <TableCell>{member.name}</TableCell>
                    <TableCell>{member.email}</TableCell>
                    <TableCell>{member.description}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Box>
    </div>
  );
};

export default Workspace;