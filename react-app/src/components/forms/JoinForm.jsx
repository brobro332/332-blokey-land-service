import React, { useState, useEffect } from 'react';
import CustomInput from '../tags/CustomInput';
import InputWithButton from '../contents/InputWithButton';
import { useNavigate } from 'react-router-dom';

const JoinForm = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [certification, setCertification] = useState("");
  const [password, setPassword] = useState("");
  const [confirmation, setConfirmation] = useState("");
  const [nickname, setNickname] = useState("");
  const [isEntered, setIsEntered] = useState(false);
  const [isConfirmable, setIsConfirmable] = useState(false);
  const [isAllEntered, setIsAllEntered] = useState(false);

  let isSended = false;

  useEffect(() => {
    if (email.trim() !== '') {
      setIsEntered(true);
    } else {
      setIsEntered(false);
    }
  }, [email]);

  useEffect(() => {
    if (isSended && password !== '') {
      setIsConfirmable(true);
    } else {
      setIsConfirmable(false);
    }
  }, [isSended, password]);

  const handleJoin = () => {
    // 회원가입 로직 (API 호출 등)
  };

  const handleCertificationButtonClick = () => {
    isSended = true;
  };

  const handleConfirmationButtonClick = () => {

  };

  const handleButtonClick = () => {
    navigate(`/join-complete-form?nickname= + ${nickname}`);
  };

  useEffect(() => {
    const allFieldsFilled =
      email.trim() !== "" &&
      certification.trim() !== "" &&
      password.trim() !== "" &&
      confirmation.trim() !== "" &&
      nickname.trim() !== "";
    
    setIsAllEntered(allFieldsFilled);
  }, [email, certification, password, confirmation, nickname]);

  return (
    <div>
      <div style={styles.container}>
        <h3>CO-WORKING</h3>
        <form
          onSubmit={(e) => {
          e.preventDefault();
          handleJoin();
        }}
          style={styles.form}
        >
          {/* 이메일 입력 */}
          <InputWithButton 
            type="text"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="이메일"
            inputStyle={
              {
                ...styles.input,
                ...styles.shortInput
              }
            }
            buttonStyle={
              {
                ...styles.affirmationButton,
                float: "right",
                ...(!isEntered ? styles.disabledButton : {})
              }
            }
            onClick={handleCertificationButtonClick}
            buttonText="인증"
          />

          {/* 인증번호 입력 */}
          <InputWithButton 
            type="text"
            value={certification}
            onChange={(e) => setCertification(e.target.value)}
            placeholder="인증번호"
            inputStyle={
              {
                ...styles.input,
                ...styles.shortInput
              }
            }
            buttonStyle={
              {
                ...styles.affirmationButton,
                float: "right",
                ...(!isConfirmable ? styles.disabledButton : {})
              }
            }
            onClick={handleConfirmationButtonClick}
            buttonText="확인"
          />

          {/* 비밀번호 입력 */}
          <CustomInput
            type="text"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="비밀번호"
            style={styles.input}
            />

          {/* 비밀번호확인 입력 */}
          <CustomInput
            type="text"
            value={confirmation}
            onChange={(e) => setConfirmation(e.target.value)}
            placeholder="비밀번호확인"
            style={styles.input}
            />

          {/* 닉네임 입력 */}
          <CustomInput
            type="text"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            placeholder="닉네임"
            style={styles.input}
          />
        </form>
        <div style={styles.buttons}>
          {/* 취소 버튼 */}
          <button type="submit" style={styles.cancelButton}>
            취소
          </button>

          {/* 가입 버튼 */}
          <button 
            type="button" 
            style={
              {
                ...styles.affirmationButton,
                ...(!isAllEntered ? styles.disabledButton : {})
              }
            } 
            disabled={!isAllEntered}
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
    form: {
      display: "flex",
      flexDirection: "column",
      gap: "15px",
    },
    input: {
      padding: "10px",
      fontSize: "16px",
      border: "1px solid #ccc",
      borderRadius: "4px",
    },
    shortInput: {
      width: "80%"
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
      marginTop: '20px',
      display: 'flex',           
      justifyContent: 'space-between',
      width: '100%'             
    }
};

export default JoinForm;