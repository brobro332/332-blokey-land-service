import React, { useState } from "react";
import {
  Box,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Paper,
  Pagination,
  IconButton,
  Menu,
  MenuItem,
} from "@mui/material";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import axios from "axios";

const MemberTable = ({ memberList, page, selectedItem, onPageChange, onChange }) => {
  const [anchorEl, setAnchorEl] = useState(null);
  const [selectedRow, setSelectedRow] = useState(null);

  const pageSize = 10;
  const startIndex = (page - 1) * pageSize;
  const currentPageData = memberList.slice(startIndex, startIndex + pageSize);

  const updateWorkspace = async (row) => {
    try {
      const result = await axios.put(
        "http://localhost:8080/api/v1/workspace/" + selectedItem.id,
        { leader: row.email },
        {
          headers: {
            "Content-Type": "application/json; charset=UTF-8",
          },
          withCredentials: true
        }
      );

      if (result.status === 200) {
        onChange();
      }
    } catch (e) {
      console.error(e);
    }
  };

  const removeMemberFromWorkspace = async (row) => {
    try {
      const result = await axios.delete(
        "http://localhost:8080/api/v1/workspace/" + row.email + "/" + selectedItem.id,
        {
          headers: {
            "Content-Type": "application/json; charset=UTF-8",
          },
          withCredentials: true
        }
      );

      if (result.status === 200) {
        onChange();
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleMenuOpen = (event, row) => {
    setAnchorEl(event.currentTarget);
    setSelectedRow(row);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    setSelectedRow(null);
  };

  return (
    <Box sx={{ marginTop: "20px" }}>
      <TableContainer component={Paper}>
        <Table sx={{ tableLayout: "fixed", width: "100%" }}>
          <TableHead>
            <TableRow>
              <TableCell sx={{ width: "20%" }}>멤버명</TableCell>
              <TableCell sx={{ width: "30%" }}>이메일</TableCell>
              <TableCell sx={{ width: "40%" }}>소개</TableCell>
              <TableCell sx={{ width: "10%" }}>관리</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {currentPageData.map((member) => (
              <TableRow key={member.email} sx={{ height: "30px" }}>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {member.leader === member.email ? "👑 " : ""}
                  {member.name}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {member.email}
                </TableCell>
                <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                  {member.description}
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
                    <MenuItem onClick={() => updateWorkspace(member)}>
                      리더 지정
                    </MenuItem>
                    <MenuItem onClick={() => removeMemberFromWorkspace(member)}>
                      추방
                    </MenuItem>
                  </Menu>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
        <Pagination
          count={Math.ceil(memberList.length / 10)}
          page={page}
          color="primary"
          sx={{
            height: "50px",
            display: "flex",
            justifyContent: "center",
          }}
          onChange={onPageChange}
        />
      </TableContainer>
    </Box>
  );
};

export default MemberTable;