import React, { useEffect } from 'react';
import { Typography } from '@mui/material';
import axios from 'axios';

const Workspace = () => {
  useEffect(() => {
    const handleOnload = async () => {
        try {
            const result = await axios.post(
                "http://localhost:8080/api/v1/team",
                { 
                  name: '김진형'
                },
                { 
                  headers: {
                    'Content-Type': 'application/json; charset=UTF-8',
                  }, 
                  withCredentials: true
                },
              );
    
          if (result.status === 200) {
            console.log(result.data);
          } else {
            console.error('Unexpected response status:', result.status);
          }
        } catch (e) {
          console.error('Error fetching team data:', e);
        }
      };

    handleOnload();
  }, []);

  return (
    <div>
      <Typography variant="h5" sx={{ marginBottom: '20px' }}>
        워크스페이스
      </Typography>
      <Typography variant="body1">여기는 워크스페이스 페이지입니다.</Typography>
    </div>
  );
};

export default Workspace;