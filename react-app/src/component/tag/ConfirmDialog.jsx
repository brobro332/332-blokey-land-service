import React from 'react';
import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from '@mui/material';

const ConfirmDialog = ({ open, onClose, title, content }) => {
  return (
    <Dialog open={open} onClose={() => onClose(false)}>
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <DialogContentText>
          {content.split('\n').map((str, index) => (
            <span key={index}>
              {str}
              <br />
            </span>
          ))}
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={() => onClose(true)} color="primary" variant="contained">
          확인
        </Button>
        <Button onClick={() => onClose(false)} color="inherit">
          취소
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ConfirmDialog;