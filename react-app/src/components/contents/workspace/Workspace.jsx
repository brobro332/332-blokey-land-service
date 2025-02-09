import React, { useEffect, useState } from "react";
import { Box, Card, Chip, Divider, Typography } from "@mui/material";
import CreateWorkspace from "./CreateWorkspace";
import SelectWorkspace from "./SelectWorkspace";
import MemberTable from "./MemberTable";
import ManageJoin from "./invitation/ManageJoin";
import axios from "axios";
import ConfirmDialog from "../../tags/ConfirmDialog";

const Workspace = () => {
  const [items, setItems] = useState([]);
  const [selectedItem, setSelectedItem] = useState(null);
  const [memberList, setMemberList] = useState([]);
  const [page, setPage] = useState(1);
  const [isCreating, setIsCreating] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [isAdding, setIsAdding] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const fetchWorkspaceList = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/v1/workspace", {
        withCredentials: true,
      });
      if (response.status === 200) {
        const workspaceList = response.data.data;

        if (workspaceList.length > 0) {
          setItems(workspaceList);
          setSelectedItem(workspaceList[0]);
        }
      }
    } catch (e) {
      console.error(e);
    }
  };
  
  const deleteWorkspace = async () => {
    try {
      const resultObject = await axios.delete(
        "http://localhost:8080/api/v1/workspace/" + selectedItem.id,
        {
          withCredentials: true
        }
      );
      if (resultObject.status === 200) {
        fetchWorkspaceList();
        setIsDialogOpen(false);
      }
    } catch (e) {
      console.error(e);
    } 
  };

  useEffect(() => {
    fetchWorkspaceList();
  }, []);

  useEffect(() => {
    const fetchMembers = async () => {
      if (selectedItem !== null) {
        try {
          const memberListObject = await axios.get(
            "http://localhost:8080/api/v1/member/memberList-in-workspace",
            {
              params: { id: selectedItem.id },
              withCredentials: true,
            }
          );
          if (memberListObject.status === 200) {
            const memberList = memberListObject.data.data;

            setPage(memberList.length);
            setMemberList(memberList);
          }
        } catch (e) {
          console.error(e);
        }
      }
    };

    fetchMembers();
  }, [selectedItem]);

  const handleCreateWorkspace = () => {
    setIsCreating(true);
    setIsEditing(false);
  };

  const handleEditWorkspace = () => {
    setIsEditing(true);
    setIsCreating(false);
  };

  const handleAddMember = () => {
    setIsAdding(true);
  };

  const handleCancelAdd = () => {
    setIsAdding(false);
  }

  const handleDeleteButtonClick = () => {
    setIsDialogOpen(true);
  };

  const handleCancel = () => {
    setIsCreating(false);
    setIsEditing(false);
  };

  const handleWorkspaceCreated = () => {
    setIsCreating(false);
    fetchWorkspaceList();
  };

  const handleWorkspaceUpdated = () => {
    setIsEditing(false);
    fetchWorkspaceList();
  };

  const handleWorkspaceDeleted = () => {
    deleteWorkspace();
  };

  const handleCancelDelete = () => {
    setIsDialogOpen(false);
  };

  return (
    <Box sx={{ padding: "20px" }}>
      <Typography variant="h6" sx={{ marginBottom : '10px' }}>워크스페이스</Typography>
      <Divider sx={{ marginBottom : '15px' }}/>
      {isCreating || isEditing ? (
        <CreateWorkspace
          onCancel={handleCancel}
          onWorkspaceCreated={handleWorkspaceCreated}
          onWorkspaceUpdated={handleWorkspaceUpdated}
          selectedItem={selectedItem}
          isEditing={isEditing}
        />
      ) : (
        <>
          {isAdding ? (
            <ManageJoin
              onCancel={handleCancelAdd}
              selectedItem={selectedItem.id} 
            />
          ) : (
            <>
              <SelectWorkspace
                items={items}
                onCreateWorkspace={handleCreateWorkspace}
                onEditWorkspace={handleEditWorkspace}
                onAddMember={handleAddMember}
                handleDeleteButtonClick={handleDeleteButtonClick}
                selectedItem={selectedItem}
                setSelectedItem={setSelectedItem}
                memberList={memberList}
                page={page}
              />
              {items.length > 0 && (
                <>
                  <Card variant="outlined" sx={{ padding: 2, marginTop: 2, maxWidth: 600 }}>
                    <Typography variant="body2">
                      <Chip label="설명" color="primary" variant="outlined" />
                      {' '}{selectedItem?.description}
                    </Typography>
                  </Card>
                  <ConfirmDialog 
                    open={isDialogOpen}
                    onConfirm={handleWorkspaceDeleted}
                    onClose={handleCancelDelete}
                    title={'워크스페이스 삭제'} 
                    content={'해당 워크스페이스를 정말 삭제하시겠습니까?\n삭제된 워크스페이스는 복구할 수 없습니다.'}
                  />
                  <MemberTable memberList={memberList} page={page} />
                </>
              )}   
            </>
          )}
        </>
      )}
    </Box>
  );
};

export default Workspace;