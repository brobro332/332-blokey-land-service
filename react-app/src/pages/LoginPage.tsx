import { useState, useEffect } from "react";
import logo from "../assets/logo.png";
import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Pagination, Navigation } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import intro1 from "../assets/intro-1.png";
import intro2 from "../assets/intro-1.png";
import intro3 from "../assets/intro-1.png";
import { apiAxios } from "../utils/Api";
import { useNavigate } from "react-router-dom";

const images = [intro1, intro2, intro3];

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);

  const navigate = useNavigate();

  // 페이지 로드 시 localStorage에서 저장된 이메일 불러오기
  useEffect(() => {
    const savedEmail = localStorage.getItem("savedEmail");
    if (savedEmail) {
      setEmail(savedEmail);
      setRememberMe(true);
    }
  }, []);

  const handleLogin = async () => {
    try {
      await apiAxios("/accounts/session", {
        method: "POST",
        data: { email, password },
        withCredentials: true,
      });

      // 로그인 성공 시 아이디 저장 여부에 따라 처리
      if (rememberMe) {
        localStorage.setItem("savedEmail", email);
      } else {
        localStorage.removeItem("savedEmail");
      }

      navigate("/main");
    } catch (err) {
      alert("로그인에 실패했습니다.");
    }
  };

  const handleJoinClick = () => {
    navigate("/join");
  };

  return (
    <div className="font-stardust min-h-screen flex items-center justify-center bg-gray-100 px-4">
      <div className="w-full max-w-5xl flex bg-white rounded-2xl shadow-lg overflow-hidden">
        <div className="w-1/2 p-10 flex flex-col justify-center">
          <div className="flex justify-center mb-8">
            <img src={logo} alt="Blokey-land Logo" className="w-42 h-28" />
          </div>

          <div className="space-y-4">
            <input
              type="text"
              placeholder="이메일"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
            />
            <input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
            />

            <div className="flex items-center justify-between text-sm">
              <label className="flex items-center cursor-pointer select-none">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="mr-2"
                />
                아이디 저장
              </label>
            </div>

            <div className="flex space-x-4 mt-4">
              <button
                onClick={handleLogin}
                className="w-full py-3 rounded-lg bg-gradient-to-r from-orange-400 to-green-500 text-white font-semibold hover:opacity-90"
              >
                로그인
              </button>
              <button
                onClick={handleJoinClick}
                className="w-full py-3 rounded-lg border border-orange-400 text-orange-500 font-semibold hover:bg-orange-50"
              >
                회원가입
              </button>
            </div>
          </div>
        </div>

        <div className="w-1/2 relative">
          <Swiper
            modules={[Autoplay, Pagination, Navigation]}
            autoplay={{ delay: 4000, disableOnInteraction: false }}
            pagination={{ clickable: true }}
            navigation
            loop
            className="h-full"
          >
            {images.map((src, idx) => (
              <SwiperSlide key={idx}>
                <div
                  className="h-full w-full bg-cover bg-center flex flex-col items-center justify-center text-white"
                  style={{ backgroundImage: `url(${src})` }}
                />
              </SwiperSlide>
            ))}
          </Swiper>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
