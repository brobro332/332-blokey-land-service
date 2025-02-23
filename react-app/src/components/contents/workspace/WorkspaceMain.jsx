import React, { useCallback, useEffect, useState } from "react";
import { Box, Card, Chip, Divider, Typography } from "@mui/material";
import CreateWorkspace from "./workspace/CreateWorkspace";
import ReadWorkspace from "./workspace/ReadWorkspace";
import MemberTable from "./workspace/MemberTable";
import ManageJoin from "./invitation/ManageJoin";
import axios from "axios";
import ConfirmDialog from "../../tags/ConfirmDialog";

const WorkspaceMain = () => {
  const [workspaceList, setWorkspaceList] = useState([]);
  const [selectedWorkspace, setSelectedWorkspace] = useState(null);
  const [memberList, setMemberList] = useState([]);
  const [page, setPage] = useState(1);
  const [isCreating, setIsCreating] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [isAdding, setIsAdding] = useState(false);
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const readWorkspaceList = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/v1/workspace/workspaceList", {
        withCredentials: true,
      });
      if (response.status === 200) {
        const workspaceList = response.data.data;

        if (workspaceList.length > 0) {
          setWorkspaceList(workspaceList);
          setSelectedWorkspace(workspaceList[0]);
        }
      }
    } catch (e) {
      console.error(e);
    }
  };
  
  const deleteWorkspace = async () => {
    try {
      const resultObject = await axios.delete(
        "http://localhost:8080/api/v1/workspace/" + selectedWorkspace.id,
        {
          withCredentials: true
        }
      );
      if (resultObject.status === 200) {
        readWorkspaceList();
        setIsDialogOpen(false);
      }
    } catch (e) {
      console.error(e);
    } 
  };

  useEffect(() => {
    readWorkspaceList();
  }, []);

  const readMemberListInWorkspace = useCallback(async () => {
    if (selectedWorkspace !== null) {
      try {
        const result = await axios.get(
          "http://localhost:8080/api/v1/member/memberList-in-workspace",
          {
            params: { id: selectedWorkspace.id },
            withCredentials: true,
          }
        );
        if (result.status === 200) {
          const memberList = result.data.data;

          setPage(1);
          setMemberList(memberList);
        }
      } catch (e) {
        console.error(e);
      }
    }
  }, [selectedWorkspace]);

  useEffect(() => {
    readMemberListInWorkspace();
  }, [readMemberListInWorkspace]);

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
    readWorkspaceList();
  };

  const handleWorkspaceUpdated = () => {
    setIsEditing(false);
    readWorkspaceList();
  };

  const handleWorkspaceDeleted = () => {
    deleteWorkspace();
  };

  const handleCancelDelete = () => {
    setIsDialogOpen(false);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
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
          workspace={selectedWorkspace}
          isEditing={isEditing}
        />
      ) : (
        <>
          {isAdding ? (
            <ManageJoin
              onCancel={handleCancelAdd}
              workspace={selectedWorkspace.id} 
            />
          ) : (
            <>
              <ReadWorkspace
                items={workspaceList}
                onCreateWorkspace={handleCreateWorkspace}
                onEditWorkspace={handleEditWorkspace}
                onAddMember={handleAddMember}
                handleDeleteButtonClick={handleDeleteButtonClick}
                workspace={selectedWorkspace}
                setWorkspace={setSelectedWorkspace}
                memberList={memberList}
                page={page}
              />
              {workspaceList.length > 0 && (
                <>
                  <Card variant="outlined" sx={{ padding: 2, marginTop: 2, maxWidth: 600 }}>
                    <Typography variant="body2">
                      <Chip label="설명" color="primary" variant="outlined" />
                      {' '}{selectedWorkspace?.description}
                    </Typography>
                  </Card>
                  <ConfirmDialog 
                    open={isDialogOpen}
                    onConfirm={handleWorkspaceDeleted}
                    onClose={handleCancelDelete}
                    title={'워크스페이스 삭제'} 
                    content={'해당 워크스페이스를 정말 삭제하시겠습니까?\n삭제된 워크스페이스는 복구할 수 없습니다.'}
                  />
                  <MemberTable 
                    memberList={memberList} 
                    page={page}
                    workspace={selectedWorkspace} 
                    onPageChange={handlePageChange}
                    onChange={readMemberListInWorkspace}
                  />
                </>
              )}   
            </>
          )}
        </>
      )}
    </Box>
  );
};

export default WorkspaceMain;