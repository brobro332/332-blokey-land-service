import { Box, Typography, Divider } from "@mui/material";

export default function Profile() {
  return (
    <Box sx={{ padding: "20px" }}>
      <Typography variant="h6" sx={{ marginBottom : '10px' }}>프로필</Typography>
      <Divider sx={{ marginBottom : '15px' }}/>
    </Box>
  );
}