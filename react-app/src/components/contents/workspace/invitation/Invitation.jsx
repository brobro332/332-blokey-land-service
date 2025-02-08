import React, { useState } from "react";
import { Box, MenuItem, Button, FormControl, InputLabel, Select, TextField, Table, TableContainer, Pagination, TableCell, TableBody, TableRow, TableHead, Paper } from "@mui/material";

const Invitation = () => {
  const [page, setPage] = useState(0);

  return (
    <Box>
      <Box display="flex" gap={2}>
        <FormControl size="small" sx={{ flex: 1 }}>
          <InputLabel>구분</InputLabel>
          <Select defaultValue="">
            <MenuItem value="option1">발신</MenuItem>
            <MenuItem value="option2">수신</MenuItem>
          </Select>
        </FormControl>
        <TextField label="아이디" variant="outlined" size="small" sx={{ flex: 1 }} />
        <TextField label="이름" variant="outlined" size="small" sx={{ flex: 1 }} />
        <Button variant="contained" color="primary">조회</Button>
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
              {invitations.map((invitation) => (
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
                    onClick={(event) => handleMenuOpen(event, member)}
                  >
                    <MoreVertIcon />
                  </IconButton>
                  {/* 드롭다운 메뉴 */}
                  <Menu
                    anchorEl={anchorEl}
                    open={selectedRow === member}
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
                    <MenuItem onClick={() => console.log("Approve", member.name)}>
                      수락
                    </MenuItem>
                    <MenuItem onClick={() => console.log("Reject", member.name)}>
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
            onChange={(event, value) => setPage(value)}
          />
        </TableContainer>
      </Box>
    </Box>
  );
};

export default Invitation;