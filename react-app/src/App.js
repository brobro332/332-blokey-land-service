import React from 'react';
import LoginForm from './components/forms/LoginForm';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import JoinForm from './components/forms/JoinForm';
import Welcome from './components/pages/Welcome';
import Main from './components/pages/Main';

const App = () => {
  return (
    <Router>
      <Routes>
        {/* 로그인 및 회원가입 */}
        <Route path="/" element={<LoginForm />} />
        <Route path="/join-form" element={<JoinForm />} />
        <Route path="/welcome" element={<Welcome />} />
        {/* 로그인 이후 */}
        <Route path="/main" element={<Main />} />
      </Routes>
    </Router>
  );
};

export default App;