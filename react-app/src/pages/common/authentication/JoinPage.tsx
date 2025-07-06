import { useState } from "react";
import logo from "../../../assets/image/logo.png";
import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Pagination, Navigation } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import intro1 from "../../../assets/image/intro-4.png";
import intro2 from "../../../assets/image/intro-5.png";
import intro3 from "../../../assets/image/intro-6.png";
import { apiAxios } from "../../../utils/tsx/Api";
import { useNavigate } from "react-router-dom";

const images = [intro1, intro2, intro3];

const JoinPage = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nickname, setNickname] = useState("");
  const [bio, setBio] = useState("");

  /**
   * @description 회원가입
   */
  const joinAccount = async () => {
    try {
      await apiAxios("/api/accounts", {
        method: "POST",
        data: {
          email,
          password,
          nickname,
          bio,
          type: "BLOKEY_LAND",
        },
        withCredentials: true,
      });

      alert("회원가입이 완료되었습니다!");
      navigate("/login");
    } catch (err) {
      alert("회원가입에 실패했습니다.");
    }
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
            <input
              type="text"
              placeholder="닉네임"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-400"
            />
            <textarea
              placeholder="한 줄 소개"
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-green-400"
              rows={3}
            />

            <div className="flex space-x-4 mt-4">
              <button
                onClick={joinAccount}
                className="w-full py-3 rounded-lg bg-gradient-to-r from-green-400 to-orange-500 text-white font-semibold hover:opacity-90"
              >
                회원가입
              </button>
              <button
                onClick={() => navigate("/login")}
                className="w-full py-3 rounded-lg border border-orange-400 text-orange-500 font-semibold hover:bg-orange-50"
              >
                뒤로가기
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
                  className="h-full w-full bg-cover bg-center flex items-center justify-center text-white"
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

export default JoinPage;
