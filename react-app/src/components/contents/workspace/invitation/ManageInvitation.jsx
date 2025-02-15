import React, { useEffect, useState } from "react";
import { FormControl, InputLabel, MenuItem, Select, TextField, Box, TableContainer, Table, TableHead, TableRow, TableCell, TableBody, Pagination, Paper, IconButton, Menu } from "@mui/material";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import axios from "axios";

const ManageInvitation = ({ selectedItem }) => {
  const [page, setPage] = useState(0);
  const [division, setDivision] = useState('ALL');
  const [email, setEmail] = useState('');
  const [name, setName] = useState('');
  const [invitationList, setInvitationList] = useState([]);
  const [anchorEl, setAnchorEl] = useState(null);
  const [selectedRow, setSelectedRow] = useState(null);

  const pageSize = 10;
  const startIndex = (page - 1) * pageSize;
  const currentPageData = invitationList.slice(startIndex, startIndex + pageSize);

  useEffect(() => {
    const fetchInvitationList = async () => {
      try {
        const result = await axios.get(
          "http://localhost:8080/api/v1/invitation",
          {
            params: { 
              email: email?.trim(),
              name: name?.trim(),
              workspaceId: selectedItem,
              division: division?.trim() === 'ALL' ? '' : division,
              menu: 'WORKSPACE'
             },
            withCredentials: true,
          }
        );
        if (result.status === 200) {
          const invitationList = result.data.data;

          setPage(1);
          setInvitationList(invitationList);
        }
      } catch (e) {
        console.error(e);
      }
    }
    
    fetchInvitationList();
  }, [division, email, name, selectedItem]);

  const handleMenuOpen = (event, row) => {
    setAnchorEl(event.currentTarget);
    setSelectedRow(row);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    setSelectedRow(null);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  return (
    <Box>
      <Box display="flex" gap={2} sx={{ marginBottom:"15px" }}>
        <FormControl size="small" sx={{ flex: 1 }}>
        <InputLabel>구분</InputLabel>
        <Select value={division} onChange={(e) => setDivision(e.target.value)}>
            <MenuItem value="ALL">전체</MenuItem>
            <MenuItem value="WORKSPACE">발신</MenuItem>
            <MenuItem value="MEMBER">수신</MenuItem>
        </Select>
        </FormControl>
        <TextField 
        label="이메일" 
        variant="outlined" 
        size="small" 
        sx={{ flex: 1 }}
        value={email}
        onChange={(e) => setEmail(e.target.value)} 
        />
        <TextField 
        label="이름" 
        variant="outlined" 
        size="small" 
        sx={{ flex: 1 }} 
        value={name}
        onChange={(e) => setName(e.target.value)}
        />
      </Box>
      <Box>
        <TableContainer component={Paper}>
          <Table sx={{ tableLayout: "fixed", width: "100%" }}>
            <TableHead>
              <TableRow>
                <TableCell sx={{ width: "10%" }}>구분</TableCell>
                <TableCell sx={{ width: "30%" }}>이메일</TableCell>
                <TableCell sx={{ width: "20%" }}>이름</TableCell>
                <TableCell sx={{ width: "30%" }}>소개</TableCell>
                <TableCell sx={{ width: "10%" }}>처리</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {currentPageData.map((invitation) => (
              <TableRow key={invitation.id} sx={{ height: "30px" }}>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {invitation.division}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {invitation.email}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {invitation.name}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {invitation.description}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  <IconButton
                    color="inherit"
                    onClick={(event) => handleMenuOpen(event, invitation)}
                  >
                    <MoreVertIcon />
                  </IconButton>
                  {/* 드롭다운 메뉴 */}
                  <Menu
                    anchorEl={anchorEl}
                    open={selectedRow === invitation}
                    onClose={handleMenuClose}
                    anchorOrigin={{
                      vertical: "top",
                      horizontal: "right",
                    }}
                    transformOrigin={{
                      vertical: "top",
                      horizontal: "right",
                    }}
                  >
                    <MenuItem onClick={() => console.log("Approve", invitation.name)}>
                      수락
                    </MenuItem>
                    <MenuItem onClick={() => console.log("Reject", invitation.name)}>
                      거절
                    </MenuItem>
                  </Menu>
                </TableCell>
              </TableRow>
              ))}
            </TableBody>
          </Table>
          <Pagination
            page={page}
            color="primary"
            sx={{
              height: "50px",
              display: "flex",
              justifyContent: "center",
            }}
            onChange={handlePageChange}
          />
        </TableContainer>
      </Box>
    </Box>
  );
};

export default ManageInvitation;