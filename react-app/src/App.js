import React from 'react';
import LoginForm from './components/form/LoginForm';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import JoinForm from './components/form/JoinForm';
import Welcome from './components/contents/Welcome';
import Main from './components/contents/Main';

const App = () => {
  return (
    <Router>  {/* 애플리케이션을 Router로 감싸기 */}
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