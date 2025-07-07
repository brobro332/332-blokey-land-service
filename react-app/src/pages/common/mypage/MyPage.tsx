import MyPageOffersSection from "../../../components/common/mypage/MyPageOffersSection";
import MyPageProfileSection from "../../../components/common/mypage/MyPageProfileSection";

const MyPage: React.FC = () => {
  return (
    <div className="w-full flex space-x-8">
      <div className="w-1/2">
        <h2 className="text-xl font-bold mb-4">프로필</h2>
        <MyPageProfileSection />
      </div>

      <div className="w-1/2">
        <h2 className="text-xl font-bold mb-4">제안</h2>
        <MyPageOffersSection />
      </div>
    </div>
  );
};

export default MyPage;
