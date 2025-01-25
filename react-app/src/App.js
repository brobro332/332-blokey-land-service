import React, { useState } from 'react';
import { Container, AppBar, Toolbar, Typography, Box, Drawer, List, ListItem, ListItemText, CssBaseline, IconButton } from '@mui/material';
import AppsIcon from '@mui/icons-material/Apps';
import LoginForm from './components/form/LoginForm';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

const App = () => {
  const [selectedMenu, setSelectedMenu] = useState('대시보드');

  const handleMenuClick = (menuName) => {
    setSelectedMenu(menuName);
  };

  return (
    <Router>  {/* 애플리케이션을 Router로 감싸기 */}
      <Routes>
        <Route path="/" element={<LoginForm />} />
      </Routes>
    </Router>
  );

  return <LoginForm></LoginForm>;

  return (
    <Box sx={{ display: 'flex' }}>
      {/* 기본 스타일 초기화 */}
      <CssBaseline />

      {/* 고정 헤더 */}
      <AppBar position='fixed' sx={{ 
          width: '100%'
        , zIndex: 1201
        , backgroundColor: '#3D3D3D' 
        }}>
        <Toolbar>
          <img 
            src='/resources/logo.png' 
            alt='logo' 
            width='30px' 
            height='30px' 
            style={{
              bottom: '0'
            }}>
          </img>
          <Typography 
            variant='h6'
            style={{
              paddingLeft: '10px'
            }}>
            삼사미 프로젝트 매니저
          </Typography>
          <IconButton color='inherit' sx={{ ml: 'auto' }}>
            <AppsIcon />
          </IconButton>
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
            top: 64,
            height: 'calc(100% - 64px)',
            color: 'white',
            backgroundColor: '#3D3D3D'
          },
        }}
        variant='permanent'
        anchor='left'
      >
        <List sx={{ size: 'lg' }}>
          <ListItem button onClick={() => handleMenuClick('대시보드')}>
            <ListItemText primary='대시보드' />
          </ListItem>
          <ListItem button onClick={() => handleMenuClick('타임라인')}>
            <ListItemText primary='타임라인' />
          </ListItem>
          <ListItem button onClick={() => handleMenuClick('프로젝트')}>
            <ListItemText primary='프로젝트' />
          </ListItem>
          <ListItem button onClick={() => handleMenuClick('스프린트')}>
            <ListItemText primary='스프린트' />
          </ListItem>
        </List>
      </Drawer>

      {/* 메인 콘텐츠 영역 */}
      <Box
        component='main'
        sx={{
          bgcolor: 'background.default',
          pt: 10
        }}
      >
        {/* 메인 컨텐츠 */}
        <Container sx={{ maxWidth: 'lg' }}>
          <Typography variant='h4'></Typography>
          <Typography variant='body1'></Typography>
        </Container>
      </Box>
    </Box>
  );
};

export default App;