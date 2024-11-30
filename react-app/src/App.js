import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginForm from "./components/forms/LoginForm";
import JoinForm from "./components/forms/JoinForm";

function App() {
  return (
    <Router>
      <Routes>
        {/* 로그인 페이지 */}
        <Route path="/" element={<LoginForm />} />
        
        {/* 회원가입 페이지 */}
        <Route path="/joinForm" element={<JoinForm />} />
      </Routes>
    </Router>
  );
}

export default App;