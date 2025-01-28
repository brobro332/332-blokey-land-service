import React from "react";
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
} from "@mui/material";
import MoreVertIcon from "@mui/icons-material/MoreVert";

const TeamMemberTable = ({ teamMemberList, page }) => (
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
          {teamMemberList.map((member) => (
            <TableRow key={member.email} sx={{ height: "30px" }}>
              <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                {member.name}
              </TableCell>
              <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                {member.email}
              </TableCell>
              <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                {member.description}
              </TableCell>
              <TableCell sx={{ paddingBottom: "5px", paddingTop: "5px" }}>
                <MoreVertIcon sx={{ marginTop: "6px" }} />
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <Pagination
        count={(Math.max(teamMemberList.length, 1) - 1) / 10 + 1}
        page={page}
        color="primary"
        sx={{
          height: "50px",
          display: "flex",
          justifyContent: "center",
        }}
      />
    </TableContainer>
  </Box>
);

export default TeamMemberTable;