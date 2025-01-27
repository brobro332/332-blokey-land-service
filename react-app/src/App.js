import React from 'react';
import LoginForm from './component/form/LoginForm';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import JoinForm from './component/form/JoinForm';
import Welcome from './component/page/Welcome';
import Main from './component/page/Main';

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