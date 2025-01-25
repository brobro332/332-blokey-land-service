import React from 'react';
import { Container, AppBar, Toolbar, Typography, Box, Drawer, List, ListItem, ListItemText, CssBaseline } from '@mui/material';

const App = () => {
  return (
    <Box sx={{ display: 'flex' }}>
      {/* 기본 스타일 초기화 */}
      <CssBaseline />

      {/* 고정 헤더 */}
      <AppBar position="fixed" sx={{ width: '100%', zIndex: 1201 }}>
        <Toolbar>
          <Typography variant="h6">삼사미 프로젝트 매니저</Typography>
        </Toolbar>
      </AppBar>

      {/* 사이드바 */}
      <Drawer
        sx={{
          width: 240,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: 240,
            boxSizing: 'border-box',
            top: 64,  // AppBar 높이만큼 여백 추가
            height: 'calc(100% - 64px)',
          },
        }}
        variant="permanent"
        anchor="left"
      >
        <List sx={{ size: 'lg' }}>
          <ListItem button>
            <ListItemText primary="타임라인" />
          </ListItem>
          <ListItem button>
            <ListItemText primary="프로젝트 관리" />
          </ListItem>
          <ListItem button>
            <ListItemText primary="스프린트" />
          </ListItem>
        </List>
      </Drawer>

      {/* 메인 콘텐츠 영역 */}
      <Box
        component="main"
        sx={{
          bgcolor: 'background.default',
          pt: 10
        }}
      >
        {/* 메인 컨텐츠 */}
        <Container sx={{ maxWidth: 'lg' }}>
          <Typography variant="h4">메인 콘텐츠</Typography>
          <Typography variant="body1">여기에 콘텐츠를 추가할 수 있습니다.</Typography>
        </Container>
      </Box>
    </Box>
  );
};

export default App;