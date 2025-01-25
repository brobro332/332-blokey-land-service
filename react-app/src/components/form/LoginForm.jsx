import React from 'react';
import { Box, Typography, TextField, Button, Link } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

const LoginForm = () => {
  return (
    <Box sx={{ display: 'flex', height: '100vh', backgroundColor: '#3D3D3D', 
        alignItems: 'center' }}>
      {/* 왼쪽 여백 */}
      <Box sx={{ flex: 5 }} /> 

      {/* 로그인 폼*/}
      <Box
        sx={{
          width: '300px',
          height: '40vh',
          backgroundColor: 'white',
          padding: '20px',
          borderRadius: '8px',
          boxShadow: 3,
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
        }}
      >
        <Typography sx = {{ display: 'flex' }}>
          <img 
            src='/resources/logo.png' 
            alt='logo' 
            width='30px' 
            height='30px' 
            style={{
              bottom: '0'
            }}>
          </img>
          <Typography variant='h5' align='center' sx={{ 
              marginBottom: '20px',
              paddingLeft: '10px',
              fontWeight: '700',
              color: '#3D3D3D' 
            }}>
            삼사미 프로젝트 매니저
          </Typography>
        </Typography>
        
        {/* 입력 필드 */}
        <TextField
          label='아이디'
          variant='outlined'
          margin='normal'
          size='small'
          fullWidth
          sx={{ height: '20px' }}
        />
        <TextField
          label='비밀번호'
          type='password'
          variant='outlined'
          margin='normal'
          size='small'
          fullWidth
          sx={{ height: '20px' }}
        />
        <Button variant='contained' color='primary' fullWidth sx={{ 
          marginTop: '20px',
          marginBottom: '20px' 
        }}>
          로그인
        </Button>
        <Typography variant='body2' align='center'>
          📢 서비스 이용이 처음이신가요?{' '}
          <Link component={RouterLink} to="/signup" underline="hover">
            회원가입
          </Link>
        </Typography>
      </Box>

      {/* 오른쪽 여백 */}
      <Box sx={{ flex: 1 }} />
    </Box>
  );
};

export default LoginForm;