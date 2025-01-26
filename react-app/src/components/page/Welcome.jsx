import React, { useState, useEffect } from 'react';
import { Box, Typography, Button, LinearProgress  } from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';

const Welcome = () => {
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const timer = setTimeout(() => {
      navigate('/');
    }, 5000);

    return () => clearTimeout(timer);
  }, [navigate]);

  const handleButtonClick = () => {
    setIsLoading(true);
    navigate('/');
  };

  const location = useLocation();
  const name = new URLSearchParams(location.search).get("name");
  
  return (
    <Box sx={{ display: 'flex', height: '100vh', backgroundColor: '#3D3D3D', 
        alignItems: 'center' }}>
      {/* 왼쪽 여백 */}
      <Box sx={{ flex: 1 }} /> 

      {/* 환영 메시지 영역 */}
      <Box
        sx={{
          width: '300px',
          height: '30vh',
          backgroundColor: 'white',
          padding: '20px',
          borderRadius: '8px',
          boxShadow: 3,
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center'
        }}
      >
        <Typography sx={{ display: 'flex', alignItems: 'center' }}>
          <img
            src='/resources/logo.png'
            alt='logo'
            width='30px'
            height='30px'
          />
          <Typography
            variant='h5'
            align='center'
            sx={{
              paddingLeft: '10px',
              fontWeight: '700',
              color: '#3D3D3D',
            }}
          >
            삼사미 프로젝트 매니저
          </Typography>
        </Typography>
        <Typography
          variant='h8'
          align='left'
          sx={{
            marginTop: '20px',
            marginBottom: '20px',
            paddingLeft: '10px',
            whiteSpace: 'pre-line'
          }}
        >
          {name}{`님의 회원가입이 완료되었습니다.
          5초 후 로그인화면으로 이동합니다.`}
        </Typography>
        <Button
          variant='contained'
          onClick={handleButtonClick}
          color='primary'
          sx={{ marginTop: '20px' }}
          fullWidth
          loading={isLoading ? 'true' : 'false'}
        >
          즉시이동
        </Button>
      </Box>

      {/* 오른쪽 여백 */}
      <Box sx={{ flex: 1 }} />

      {/* LinearProgress 하단 고정 */}
      <Box sx={{ position: 'fixed', bottom: 0, width: '100%' }}>
        <LinearProgress variant="indeterminate"/>
      </Box>
    </Box>
  );
};

export default Welcome;