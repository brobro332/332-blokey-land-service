import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginForm from "./LoginForm";
import JoinForm from "./JoinForm";

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