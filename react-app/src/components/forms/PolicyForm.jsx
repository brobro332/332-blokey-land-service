import React, { useState } from 'react';
import ValidAgreeCheckbox from '../contents/ValidAgreeCheckbox';
import { useNavigate } from 'react-router-dom';

const PolicyForm = () => {
  const navigate = useNavigate();
  const [checkboxes, setCheckboxes] = useState({
    all: false,
    termsOfUse: false,
    consentToInformation: false,
  });

  const isAllChecked = Object.values(checkboxes).every(value => value === true);

  const handleCheckboxChange = (updatedCheckboxes) => {
    setCheckboxes(updatedCheckboxes);
  };

  const handleButtonClick = () => {
    navigate('/join-form');
  };
  
  return (
    <div>
      <div style={styles.container}>
        <h3>CO-WORKING</h3>
        <div style={styles.content}>
            <div>
              CO-WORKING 회원가입을 위하여 아래의 개인정보 수집이용에 대한 내용을 자세히 읽어 보신 후 동의 여부를 결정하여 주시기 바랍니다.
            </div>
            <hr style={styles.hr}/>
            <ValidAgreeCheckbox onCheckboxChange={handleCheckboxChange} />
        </div>
        <div style={styles.buttons}>
          {/* 취소 버튼 */}
          <button type="submit" style={styles.cancelButton}>
            취소
          </button>

          {/* 동의 버튼 */}
          <button 
            type="button" 
            style={
              {
                ...styles.affirmationButton,
                ...(!isAllChecked ? styles.disabledButton : {})
              }
            } 
            disabled={!isAllChecked}
            onClick={handleButtonClick}
          >
            동의
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
        marginBottom: '10px',
        padding: '20px',
        border: '1px solid #ddd',
        borderRadius: '8px',
        textAlign: 'center',
    },
    content: {
        maxWidth: '400px',
        margin: '50px auto',
        marginBottom: '10px',
        padding: '20px',
        border: '1px solid #ddd',
        borderRadius: '8px',
        textAlign: 'left',
    },
    hr: {
        backgroundColor: '#ddd',
        width: '100%',
        height: '1px',
        border: '0'
    }, 
    cancelButton: {
      padding: "10px",
      fontSize: "16px",
      color: "#fff",
      backgroundColor: "#ddd",
      border: "none",
      borderRadius: "4px",
      cursor: "pointer",
      float: "left"
    },
    affirmationButton: {
      padding: "10px",
      fontSize: "16px",
      color: "#fff",
      backgroundColor: "#0A6E0A",
      border: "none",
      borderRadius: "4px",
      cursor: "pointer",
      float: "right"
    },
    disabledButton: {
      backgroundColor: '#46BE46',
      cursor: 'not-allowed'
    },
    buttons: {
      display: 'flex',           
      justifyContent: 'space-between',
      width: '100%'             
    }
};

export default PolicyForm;