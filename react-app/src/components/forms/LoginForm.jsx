import React, { useState } from "react";
import axios from 'axios';
import CustomInput from "../tags/CustomInput";
import {
  FancyText,
  Container,
  ContainerJoin,
  StyledLink,
  Form,
  ActiveButton,
  Hr,
  GoogleLoginButton,
  KakaoLoginButton
} from '../../styles/styles';

const LoginForm = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async () => {
    try {
      const result = await axios.post(
        "http://localhost:8080/api/v1/authentication",
        { 
          email: email,
          password: password
        },
        { 
          headers: {
            'Content-Type': 'application/json; charset=UTF-8',
          }
        }
      );
  
      if (result.status === 200) {
        alert("로그인에 성공하였습니다.");
      } else {
        alert("로그인에 실패하였습니다. 관리자에게 문의해주세요.");
      }
    } catch (e) {
      alert("네트워크에 문제가 있거나 서버 오류가 발생했습니다.");
    }
  };

  return (
    <div>
      <Container>
        <FancyText>CO-WORKING</FancyText>

        <br/>

        <Form
          onSubmit={(e) => {
            e.preventDefault();
            handleLogin();
          }}
        >
          {/* 이메일 입력 */}
          <CustomInput
            type="text"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="이메일"
          />

          {/* 비밀번호 입력 */}
          <CustomInput
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="비밀번호"
          />

          {/* 로그인 버튼 */}
          <ActiveButton type="submit">
            로그인
          </ActiveButton>

          <Hr />

          {/* 구글로그인 버튼 */}
          <GoogleLoginButton type="button">
            구글로그인
          </GoogleLoginButton>

          {/* 카카오로그인 버튼 */}
          <KakaoLoginButton type="button">
            카카오로그인
          </KakaoLoginButton>
        </Form>
      </Container>
      <ContainerJoin>
        처음이신가요? <StyledLink to="/policy-form">회원가입</StyledLink>
      </ContainerJoin>
    </div>
  );
};

export default LoginForm;