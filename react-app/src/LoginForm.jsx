import React, { useState } from 'react';
import CustomInput from './CustomInput';

const LoginForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    // 로그인 로직 (여기서 API 호출 등을 처리)
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

          <hr style={styles.hr}/>

          {/* 구글로그인 버튼 */}
          <button type="submit" style={styles.googleLoginButton}>
            구글로그인
          </button>

          {/* 카카오로그인 버튼 */}
          <button type="submit" style={styles.kakaoLoginButton}>
            카카오로그인
          </button>
        </form>
      </div>
      <div style={styles.containerCreateMember}>
        처음이신가요? <a href="#link">회원가입</a>
      </div>
    </div>
  );
};

// 간단한 스타일 객체
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
  containerCreateMember: {
    maxWidth: '400px',
    margin: '50px auto',
    marginTop: '10px',
    padding: '20px',
    border: '1px solid #ddd',
    borderRadius: '8px',
    textAlign: 'center',
  },
  form: {
    display: 'flex',
    flexDirection: 'column',
    gap: '15px',
  },
  input: {
    padding: '10px',
    fontSize: '16px',
    border: '1px solid #ccc',
    borderRadius: '4px',
  },
  loginButton: {
    padding: '10px',
    fontSize: '16px',
    color: '#fff',
    backgroundColor: '#0A6E0A',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
  },
  hr: {
    backgroundColor: '#ddd',
    width: '100%',
    height: '1px',
    border: '0'
  },
  googleLoginButton: {
    padding: '10px',
    fontSize: '16px',
    color: '#fff',
    backgroundColor: '#007BFF',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',  
  },
  kakaoLoginButton: {
    padding: '10px',
    fontSize: '16px',
    backgroundColor: '#FEE500',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',  
  },
  a: {
    color: '#0A6E0A'
  }
};

export default LoginForm;