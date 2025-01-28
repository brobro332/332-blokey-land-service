import React, { useEffect, useState } from 'react';
import { Container, AppBar, Toolbar, Typography, Box, Drawer, List, ListItem, ListItemText, CssBaseline, IconButton, Snackbar, Alert, MenuItem, Menu } from '@mui/material';
import AppsIcon from '@mui/icons-material/Apps';
import CheckIcon from '@mui/icons-material/Check';
import Workspace from '../contents/workspace/Workspace';

const Main = () => {
  const [selectedMenu, setSelectedMenu] = useState('대시보드');
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [anchorEl, setAnchorEl] = useState(null);
  
  const open = Boolean(anchorEl);

  useEffect(() => {
    setOpenSnackbar(true);
  }, []);

  const handleMenuClick = (menuName) => {
    setAnchorEl(null);
    setSelectedMenu(menuName);
  };

  const handleAppsListOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleAppsListClose = () => {
    setAnchorEl(null);
  };

  const renderContent = () => {
    switch (selectedMenu) {
       case '대시보드':
        return (
          <div>
            <Typography variant="h5">대시보드</Typography>
            <Typography variant="body1">여기는 대시보드 페이지입니다.</Typography>
          </div>
        );
      case '타임라인':
        return (
          <div>
            <Typography variant="h5">타임라인</Typography>
            <Typography variant="body1">여기는 타임라인 페이지입니다.</Typography>
          </div>
        );
      case '태스크':
        return (
          <div>
            <Typography variant="h5">태스크</Typography>
            <Typography variant="body1">여기는 태스크 페이지입니다.</Typography>
          </div>
        );
      case '스프린트':
        return (
          <div>
            <Typography variant="h5">스프린트</Typography>
            <Typography variant="body1">여기는 스프린트 페이지입니다.</Typography>
          </div>
        );
      case '워크스페이스':
        return (
          <Workspace />
        );
      case '프로필':
        return (
          <div>
            <Typography variant="h5">프로필</Typography>
            <Typography variant="body1">프로필 정보를 확인하세요.</Typography>
          </div>
        );
      default:
        return (
          <div>
            <Typography variant="h5">대시보드</Typography>
            <Typography variant="body1">여기는 대시보드 페이지입니다.</Typography>
          </div>
        );
    }
  };

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
              <IconButton color='inherit' onClick={handleAppsListOpen} sx={{ ml: 'auto' }}>
            <AppsIcon />
            </IconButton>

            {/* 드롭다운 메뉴 */}
            <Menu
              anchorEl={anchorEl}
              open={open}
              onClose={handleAppsListClose}
              anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'right',
              }}
              transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right',
              }}>
              <MenuItem onClick={() => handleMenuClick('프로필')}>프로필</MenuItem>
              <MenuItem onClick={() => handleMenuClick('워크스페이스')}>워크스페이스</MenuItem>
              <MenuItem onClick={() => handleMenuClick('로그아웃')}>로그아웃</MenuItem>
            </Menu>
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
            <ListItem button='true' onClick={() => handleMenuClick('대시보드')}>
              <ListItemText primary='대시보드' />
            </ListItem>
            <ListItem button='true' onClick={() => handleMenuClick('타임라인')}>
              <ListItemText primary='타임라인' />
            </ListItem>
            <ListItem button='true' onClick={() => handleMenuClick('태스크')}>
              <ListItemText primary='태스크' />
            </ListItem>
            <ListItem button='true' onClick={() => handleMenuClick('스프린트')}>
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
            {renderContent()}
          </Container>
        </Box>
        
        {/* 메시지 알림 */}
        <Snackbar
          open={openSnackbar}
          autoHideDuration={3000}
          anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
          onClose={() => setOpenSnackbar(false)}
        >
          <Alert
            onClose={() => setOpenSnackbar(false)}
            severity="success"
            icon={<CheckIcon fontSize="inherit" />}
          >
            환영합니다.
          </Alert>
        </Snackbar>
      </Box>
    );
};

export default Main;