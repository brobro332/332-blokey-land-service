import React, { useState } from "react";
import axios from 'axios';
import { Link } from "react-router-dom";
import CustomInput from "../tags/CustomInput";

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
      <div style={styles.container}>
        <h3>CO-WORKING</h3>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleLogin();
          }}
          style={styles.form}
        >
          {/* 이메일 입력 */}
          <CustomInput
            type="text"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="이메일"
            style={styles.input}
          />

          {/* 비밀번호 입력 */}
          <CustomInput
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="비밀번호"
            style={styles.input}
          />

          {/* 로그인 버튼 */}
          <button type="submit" style={styles.loginButton}>
            로그인
          </button>

          <hr style={styles.hr} />

          {/* 구글로그인 버튼 */}
          <button type="button" style={styles.googleLoginButton}>
            구글로그인
          </button>

          {/* 카카오로그인 버튼 */}
          <button type="button" style={styles.kakaoLoginButton}>
            카카오로그인
          </button>
        </form>
      </div>
      <div style={styles.containerJoin}>
        처음이신가요? <Link to="/policy-form">회원가입</Link>
      </div>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: "400px",
    margin: "50px auto",
    marginBottom: "10px",
    padding: "20px",
    border: "1px solid #ddd",
    borderRadius: "8px",
    textAlign: "center",
  },
  containerJoin: {
    maxWidth: "400px",
    margin: "50px auto",
    marginTop: "10px",
    padding: "20px",
    border: "1px solid #ddd",
    borderRadius: "8px",
    textAlign: "center",
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
  hr: {
    backgroundColor: "#ddd",
    width: "100%",
    height: "1px",
    border: "0",
  },
  loginButton: {
    padding: "10px",
    fontSize: "16px",
    color: "#fff",
    backgroundColor: "#0A6E0A",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
  googleLoginButton: {
    padding: "10px",
    fontSize: "16px",
    color: "#fff",
    backgroundColor: "#007BFF",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
  kakaoLoginButton: {
    padding: "10px",
    fontSize: "16px",
    backgroundColor: "#FEE500",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
};

export default LoginForm;