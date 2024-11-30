import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const JoinCompleteForm = () => {
  const navigate = useNavigate();

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const nickname = queryParams.get("nickname");

  const handleButtonClick = () => {
    navigate('/');
  };
    
  return (
    <div>
      <div style={styles.container}>
        <h3>CO-WORKING</h3>
        <h4 style={
          {
            color: "#0A6E0A",
            marginBottom: "0"
          }
        }>회원가입 완료</h4>
        <div style={
            {
                ...styles.container,
                textAlign: "left",
                border: "none",
                margin: "0"
            }    
        }>
          <span style={
            {
              color: "#0A6E0A",
              fontWeight: "bold"
            }
          }>{nickname}</span>님의 회원가입이 성공적으로 완료되었습니다. <br/>
          사용자 정보 수정을 원하신다면 [사용자 정보] 메뉴를 이용해주세요. 
        </div>
        <br/>
        <div style={styles.buttons}>
          {/* 홈으로 돌아가기 버튼 */}
          <button 
            type="button" 
            style={styles.affirmationButton} 
            onClick={handleButtonClick}
          >
            홈으로 돌아가기
          </button>
        </div>
      </div>
    </div>
  );
};

const styles = {
    container: {
        maxWidth: '400px',
        margin: '50px auto',
        padding: '20px',
        border: '1px solid #ddd',
        borderRadius: '8px',
        textAlign: 'center',
    },
    affirmationButton: {
      padding: "10px",
      fontSize: "16px",
      color: "#fff",
      backgroundColor: "#0A6E0A",
      border: "none",
      borderRadius: "4px",
      cursor: "pointer",
    }
};

export default JoinCompleteForm;