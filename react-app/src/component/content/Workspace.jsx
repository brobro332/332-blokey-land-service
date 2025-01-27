import React, { useEffect, useState } from 'react';
import { Typography, Select, MenuItem, FormControl, InputLabel, Box, TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Paper, Link, TextField, Button, Pagination } from '@mui/material';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import axios from 'axios';
import { useUser } from '../hook/UserProvider';
import ConfirmDialog from '../tag/ConfirmDialog';

const CreateWorkspace = ({
  onCancel,
  onWorkspaceCreated,
  onWorkspaceUpdated,
  selectedItem,
  isEditing
}) => {
  const [name, setName] = useState(selectedItem ? selectedItem.name : '');
  const [description, setDescription] = useState(selectedItem ? selectedItem.description : '');
  const [isLoading, setIsLoading] = useState(false);

  const { user } = useUser();

  const isFormFilled = name.trim() && description.trim();

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
            'Content-Type': 'application/json; charset=UTF-8',
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
            'Content-Type': 'application/json; charset=UTF-8',
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
        label="워크스페이스 소개"
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
        sx={{ marginTop: '20px', marginBottom: '20px' }}
        disabled={!isFormFilled || isLoading}
        fullWidth
      >
        {isLoading ? (isEditing ? '수정 중...' : '생성 중...') : (isEditing ? '수정' : '생성')}
      </Button>
      <Button variant="contained" onClick={onCancel} color="inherit" fullWidth>
        취소
      </Button>
    </Box>
  );
};

const SelectWorkspace = ({ items, onCreateWorkspace, onEditWorkspace, handleDeleteButtonClick, selectedItem, setSelectedItem, teamMemberList, page }) => (
  <Box>
    {items.length > 0 ? (
      <div>
        <FormControl sx={{ width: '30%' }}>
          <InputLabel id="select-workspace">워크스페이스 선택</InputLabel>
          <Select
            labelId="select-workspace"
            id={selectedItem.id}
            size="small"
            value={selectedItem.name}
            label="워크스페이스 선택"
            onChange={(e) => {
              const selected = items.find(item => item.name === e.target.value);
              setSelectedItem(selected);
            }}
          >
            {items.map((item) => (
              <MenuItem key={item.id} value={item.name}>
                {item.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        {' '}
        <Button
          variant='contained'
          color='primary'
          onClick={onCreateWorkspace}
        >
          워크스페이스 생성
        </Button>
        <Button
          variant='contained'
          onClick={onEditWorkspace}
          color='primary'
          sx={{ marginLeft : '10px' }}
        >
          워크스페이스 편집
        </Button>
        <Button
          variant='contained'
          color='primary'
          sx={{ marginLeft : '10px' }}
        >
          멤버 추가
        </Button>
        <Button
          variant='contained'
          onClick={handleDeleteButtonClick}
          color='error'
          sx={{ marginLeft : '10px' }}
        >
          워크스페이스 삭제
        </Button>
      </div>
    ) : (
      <Box>
        생성된 워크스페이스가 없습니다.{' '}
        <Link onClick={onCreateWorkspace} underline="hover">
          워크스페이스 생성
        </Link>
      </Box>
    )}

    <Box sx={{ marginTop: '20px' }}>
      {items.length > 0 && (
        <TableContainer component={Paper}>
          <Table sx={{ tableLayout: 'fixed', width: '100%' }}>
            <TableHead>
              <TableRow>
                <TableCell sx={{ width: '20%' }}>멤버명</TableCell>
                <TableCell sx={{ width: '30%' }}>이메일</TableCell>
                <TableCell sx={{ width: '40%' }}>소개</TableCell>
                <TableCell sx={{ width: '10%' }}>관리</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {teamMemberList.map((member) => (
                <TableRow sx={{ height: '30px' }} key={member.email}>
                  <TableCell sx={{ paddingBottom : '5px', paddingTop : '5px' }}>{member.name}</TableCell>
                  <TableCell sx={{ paddingBottom : '5px', paddingTop : '5px' }}>{member.email}</TableCell>
                  <TableCell sx={{ paddingBottom : '5px', paddingTop : '5px' }}>{member.description}</TableCell>
                  <TableCell sx={{ paddingBottom : '5px', paddingTop : '5px' }}><MoreVertIcon sx={{ marginTop : '6px'}} /></TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
          <Pagination
            count={((Math.max(teamMemberList.length, 1) - 1) / 10) + 1}
            page={page}
            color="primary"
            sx={{
              height: '50px',
              display: 'flex',
              justifyContent: 'center'
            }}
          />
        </TableContainer>
      )}
    </Box>
  </Box>
);

const Workspace = () => {
  const [items, setItems] = useState([]);
  const [selectedItem, setSelectedItem] = useState(null);
  const [teamMemberList, setTeamMemberList] = useState([]);
  const [isCreating, setIsCreating] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [page, setPage] = useState(1);
  const [dialogOpen, setDialogOpen] = useState(false);

  const { user } = useUser();

  useEffect(() => {
    const fetchTeams = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/team", {
          params: { email: user.email },
          withCredentials: true,
        });
        if (response.status === 200) {
          const teamList = response.data.data;
          setItems(teamList);
          if (teamList.length > 0) {
            setSelectedItem(teamList[0]);
          }
        }
      } catch (e) {
        console.error(e);
      }
    };

    fetchTeams();
  }, [user.email, isCreating]);

  useEffect(() => {
    const fetchTeamMembers = async () => {
      if (selectedItem) {
        try {
          const memberListObject = await axios.get(
            "http://localhost:8080/api/v1/team/memberList",
            {
              params: { id: selectedItem.id },
              withCredentials: true,
            }
          );
          if (memberListObject.status === 200) {
            const memberList = memberListObject.data.data;
            setPage(memberList.length);
            setTeamMemberList(memberList);
          }
        } catch (e) {
          console.error(e);
        }
      }
    };

    fetchTeamMembers();
  }, [selectedItem]);

  const handleCreateWorkspace = () => {
    setIsCreating(true);
    setIsEditing(false);
  };

  const handleEditWorkspace = () => {
    setIsCreating(true);
    setIsEditing(true);
  };

  const handleCancelCreate = () => {
    setIsCreating(false);
    setIsEditing(false);
  };

  const handleWorkspaceCreated = (newWorkspace) => {
    if (newWorkspace !== null) {
      setItems((prev) => [...prev, newWorkspace]);
    }
    setIsCreating(false);
  };

  const handleWorkspaceUpdated = (updatedWorkspace) => {
    setItems((prev) =>
      prev.map((item) =>
        item.id === updatedWorkspace.id ? updatedWorkspace : item
      )
    );
    setIsCreating(false);
    setIsEditing(false);
  };

  const handleDeleteButtonClick = () => {
    setDialogOpen(true);
  };

  const handleCancelDelete = () => {
    setDialogOpen(false);
  };

  return (
    <div>
      <Typography variant="h5" sx={{ marginBottom: '20px' }}>
        워크스페이스
      </Typography>
      {isCreating ? (
        <CreateWorkspace
          onCancel={handleCancelCreate}
          onWorkspaceCreated={handleWorkspaceCreated}
          onWorkspaceUpdated={handleWorkspaceUpdated}
          selectedItem={isEditing ? selectedItem : null}
          isEditing={isEditing}
        />
      ) : (
        <>
          <ConfirmDialog 
            open={dialogOpen}
            onClose={handleCancelDelete}
            title={'워크스페이스 삭제'} 
            content={'해당 워크스페이스를 정말 삭제하시겠습니까?\n삭제된 워크스페이스는 복구할 수 없습니다.'}
          />
        
          <SelectWorkspace
           items={items}
           onCreateWorkspace={handleCreateWorkspace}
           onEditWorkspace={handleEditWorkspace}
           handleDeleteButtonClick={handleDeleteButtonClick}
           selectedItem={selectedItem}
           setSelectedItem={setSelectedItem}
           teamMemberList={teamMemberList}
           page={page}
          />
        </>
      )}
    </div>
  );
};

export default Workspace;