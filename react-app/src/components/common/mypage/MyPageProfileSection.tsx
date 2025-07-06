import React, { useState, useEffect } from "react";
import { apiAxios } from "../../../utils/tsx/Api";
import { Blokey } from "../../../types/blokey";

const MyPageProfileSection: React.FC = () => {
  const [blokey, setBlokey] = useState<Blokey | null>(null);
  const [editMode, setEditMode] = useState(false);
  const [nickname, setNickname] = useState("");
  const [bio, setBio] = useState("");

  /**
   * @description 사용자 정보 초기화
   */
  useEffect(() => {
    apiAxios("/blokey-land/api/blokeys/me", {
      method: "GET",
      withCredentials: true,
    }).then((res) => {
      setBlokey(res);
      setNickname(res.nickname);
      setBio(res.bio);
    });
  }, []);

  /**
   * @description 사용자 정보 수정
   */
  const updateBlokey = async () => {
    await apiAxios(`/blokey-land/api/blokeys/${blokey?.id}`, {
      method: "PATCH",
      data: { nickname, bio },
      withCredentials: true,
    });
    setBlokey({ ...blokey!, nickname, bio });
    setEditMode(false);
  };

  if (!blokey) return <div>로딩 중...</div>;

  return (
    <div className="p-4 bg-green-50 h-[540px] rounded-xl overflow-auto">
      <h3 className="text-lg font-semibold mb-4">프로필 정보</h3>

      <table
        className="w-full border border-gray-700 rounded-md bg-white text-sm"
        style={{ tableLayout: "fixed" }}
      >
        <thead className="bg-green-600 text-white font-semibold">
          <tr>
            <th
              className="border border-gray-200 px-4 py-2 text-left text-center"
              style={{ width: "30%" }}
            >
              속성
            </th>
            <th
              className="border border-gray-200 px-4 py-2 text-left text-center"
              style={{ width: "70%" }}
            >
              값
            </th>
          </tr>
        </thead>
        <tbody>
          {/* 닉네임 행 */}
          <tr>
            <td className="border border-gray-200 px-4 py-2 font-semibold bg-green-600 text-white text-center">
              닉네임
            </td>
            <td className="border border-gray-200 px-4 py-2">
              {editMode ? (
                <input
                  value={nickname}
                  onChange={(e) => setNickname(e.target.value)}
                  className="w-full border border-gray-200 rounded px-2 py-1 text-sm box-border"
                  style={{ minHeight: "2rem" }}
                />
              ) : (
                <p className="px-2 py-1 text-sm min-h-[2rem] leading-relaxed">
                  {blokey.nickname}
                </p>
              )}
            </td>
          </tr>

          {/* 소개 행 */}
          <tr>
            <td className="border border-gray-200 px-4 py-2 font-semibold bg-green-600 text-white text-center">
              소개
            </td>
            <td className="border border-gray-200 px-4 py-2">
              {editMode ? (
                <input
                  type="text"
                  value={bio}
                  onChange={(e) => setBio(e.target.value)}
                  className="w-full border border-gray-200 rounded px-2 py-1 text-sm box-border"
                  style={{ minHeight: "2rem" }}
                />
              ) : (
                <p className="px-2 py-1 text-sm min-h-[2rem] leading-relaxed">
                  {blokey.bio}
                </p>
              )}
            </td>
          </tr>
        </tbody>
      </table>

      {/* 버튼 영역 */}
      <div className="mt-4 space-x-2">
        {editMode ? (
          <>
            <button
              className="px-3 py-1 bg-green-600 text-white rounded text-md"
              onClick={updateBlokey}
            >
              저장
            </button>
            <button
              className="px-3 py-1 bg-gray-200 border border-gray-200 rounded text-md text-gray-500"
              onClick={() => setEditMode(false)}
            >
              취소
            </button>
          </>
        ) : (
          <button
            className="px-3 py-1 bg-green-600 border-gray-200 text-white rounded text-md"
            onClick={() => setEditMode(true)}
          >
            수정
          </button>
        )}
      </div>
    </div>
  );
};

export default MyPageProfileSection;
