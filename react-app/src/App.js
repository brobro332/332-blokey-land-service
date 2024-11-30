import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginForm from "./components/forms/LoginForm";
import PolicyForm from "./components/forms/PolicyForm";
import JoinForm from "./components/forms/JoinForm";
import JoinCompleteForm from "./components/forms/JoinCompleteForm";

function App() {
  return (
    <Router>
      <Routes>
        {/* 로그인 페이지 */}
        <Route path="/" element={<LoginForm />} />
        
        {/* 회원가입 약관동의 페이지 */}
        <Route path="/policy-form" element={<PolicyForm />} />

        {/* 회원가입 개인정보입력 페이지 */}
        <Route path="/join-form" element={<JoinForm />} />

        {/* 회원가입 완료 페이지 */}
        <Route path="/join-complete-form" element={<JoinCompleteForm />} />
      </Routes>
    </Router>
  );
}

export default App;