import React, { useState, useEffect } from 'react';
import axios from 'axios';
import CustomInput from '../tags/CustomInput';
import ConfirmInput from '../contents/ConfirmInput';
import { useNavigate } from 'react-router-dom';
import {
  FancyText,
  Container,
  Form,
  ActiveButton,
  ButtonsDiv,
  CancelButton
} from '../../styles/styles';

const JoinForm = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [certification, setCertification] = useState("");
  const [password, setPassword] = useState("");
  const [confirmation, setConfirmation] = useState("");
  const [name, setName] = useState("");
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

  const handleJoin = async () => {
    const body = {
      email: email,
      password: password,
      name: name
    };

    try {
      const result = await axios.post(
        "http://spring-app:8080/api/v1/member"
        , body
        , { 
          headers: {
            'Content-Type': 'application/json; charset=UTF-8'
          }
        }
      );
      if (result.status === 200) {
        navigate(`/join-complete-form?name= + ${name}`);
      } else {
        alert("회원가입에 실패하였습니다. 관리자에게 문의해주세요.");
      }
    } catch(e) {
      alert("네트워크에 문제가 있습니다.");
    }
  };

  const handleCancelButtonClick = () => {
    navigate('/');
  };

  const handleCertificationButtonClick = () => {
    isSended = true;
  };

  const handleConfirmationButtonClick = () => {

  };

  const handleButtonClick = () => {
    handleJoin();
  };

  useEffect(() => {
    const allFieldsFilled =
      email.trim() !== "" &&
      certification.trim() !== "" &&
      password.trim() !== "" &&
      confirmation.trim() !== "" &&
      name.trim() !== "";
    
    setIsAllEntered(allFieldsFilled);
  }, [email, certification, password, confirmation, name]);

  return (
    <div>
      <Container>
        <FancyText>CO-WORKING</FancyText>
        <Form
          onSubmit={(e) => {
          e.preventDefault();
        }}
        >
          {/* 이메일 입력 */}
          <ConfirmInput
            type="text"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="이메일"
            buttonDisabled={!isEntered}
            onClick={handleCertificationButtonClick}
            buttonText="인증"
          />

          {/* 인증번호 입력 */}
          <ConfirmInput 
            type="text"
            value={certification}
            onChange={(e) => setCertification(e.target.value)}
            placeholder="인증번호"
            buttonDisabled={!isConfirmable}
            onClick={handleConfirmationButtonClick}
            buttonText="확인"
          />

          {/* 비밀번호 입력 */}
          <CustomInput
            type="text"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="비밀번호"
            />

          {/* 비밀번호확인 입력 */}
          <CustomInput
            type="text"
            value={confirmation}
            onChange={(e) => setConfirmation(e.target.value)}
            placeholder="비밀번호확인"
            />

          {/* 닉네임 입력 */}
          <CustomInput
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="닉네임"
          />
        </Form>

        <br/>

        <ButtonsDiv>
          {/* 취소 버튼 */}
          <CancelButton 
            type="submit"
            onClick={handleCancelButtonClick}
          >
            취소
          </CancelButton>

          {/* 가입 버튼 */}
          <ActiveButton 
            type="button" 
            disabled={!isAllEntered}
            onClick={handleButtonClick}
          >
            동의
          </ActiveButton>
        </ButtonsDiv>
      </Container>
    </div>
  );
};

export default JoinForm;