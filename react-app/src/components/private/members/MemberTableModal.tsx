import React, { useCallback, useEffect, useState } from "react";
import { Member } from "../../../types/member";
import { apiAxios } from "../../../utils/tsx/Api";
import { RoleType } from "../../../types/common";

interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  first: boolean;
  last: boolean;
}

interface MemberTableModalProps {
  projectId: number;
  onClose: () => void;
}

const roles: RoleType[] = ["LEADER", "MANAGER", "MEMBER", "VIEWER"];

const MemberTableModal: React.FC<MemberTableModalProps> = ({
  projectId,
  onClose,
}) => {
  const [members, setMembers] = useState<Member[]>([]);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [isRoleModalOpen, setIsRoleModalOpen] = useState(false);
  const [selectedRole, setSelectedRole] = useState<RoleType>("MEMBER");

  const fetchMembers = useCallback(
    async (pageNumber: number) => {
      try {
        const res = await apiAxios<Page<Member>>(
          `/blokey-land/api/projects/${projectId}/members?page=${pageNumber}`,
          {
            method: "GET",
            withCredentials: true,
          }
        );
        setMembers(res.content);
        setTotalPages(res.totalPages);
      } catch (err) {
        console.error("멤버 조회 실패", err);
        setMembers([]);
      }
    },
    [projectId]
  );

  useEffect(() => {
    fetchMembers(page);
  }, [projectId, page, fetchMembers]);

  const handleSelect = (id: number) => {
    setSelectedId(id === selectedId ? null : id);
  };

  const handleRoleUpdate = async () => {
    if (selectedId === null) return;
    if (!selectedMember) return;

    try {
      if (selectedRole === "LEADER") {
        const currentLeader = members.find((m) => m.role === "LEADER");

        if (currentLeader && currentLeader.id !== selectedId) {
          await apiAxios(`/blokey-land/api/members/${currentLeader.id}`, {
            method: "PATCH",
            data: {
              memberId: currentLeader.id,
              role: "MANAGER",
            },
            withCredentials: true,
          });
        }
      }

      await apiAxios(`/blokey-land/api/members/${selectedId}`, {
        method: "PATCH",
        data: {
          memberId: selectedId,
          role: selectedRole,
        },
        withCredentials: true,
      });

      alert("권한이 변경되었습니다.");
      fetchMembers(page);
      setSelectedId(null);
      setIsRoleModalOpen(false);
    } catch (err) {
      console.error("권한 변경 실패", err);
      alert("권한 변경에 실패했습니다.");
    }
  };

  const handleExpel = async () => {
    if (selectedId === null) return;
    try {
      await apiAxios(`/blokey-land/api/members/${selectedId}`, {
        method: "DELETE",
        withCredentials: true,
      });
      alert("멤버가 추방되었습니다.");
      fetchMembers(page);
      setSelectedId(null);
    } catch (err) {
      console.error("추방 실패", err);
      alert("추방에 실패했습니다.");
    }
  };

  const selectedMember = members.find((m) => m.id === selectedId);
  const selectableRoles = roles.filter((role) => role !== selectedMember?.role);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-30">
      <div className="bg-white rounded-xl shadow-lg max-w-4xl w-full max-h-[90vh] overflow-auto p-6">
        <h2 className="text-xl font-semibold text-gray-800 mb-4">
          프로젝트 멤버
        </h2>

        <div className="flex justify-end space-x-2 mb-4">
          <button
            onClick={() => setIsRoleModalOpen(true)}
            disabled={selectedId === null}
            className="px-3 py-1 bg-green-600 hover:bg-green-700 text-white rounded disabled:opacity-50"
          >
            권한 변경
          </button>
          <button
            onClick={handleExpel}
            disabled={selectedId === null}
            className="px-3 py-1 bg-red-600 hover:bg-red-700 text-white rounded disabled:opacity-50"
          >
            추방
          </button>
        </div>

        <table className="w-full text-sm text-left border">
          <thead className="bg-gray-100 text-gray-700">
            <tr>
              <th className="p-2 border">선택</th>
              <th className="p-2 border">닉네임</th>
              <th className="p-2 border">소개</th>
              <th className="p-2 border">권한</th>
            </tr>
          </thead>
          <tbody>
            {members.length > 0 ? (
              members.map((member) => (
                <tr key={member.id} className="border-b">
                  <td className="p-2 border text-center">
                    <input
                      type="checkbox"
                      disabled={member.role === "LEADER"}
                      checked={selectedId === member.id}
                      onChange={() => {
                        if (member.role !== "LEADER") handleSelect(member.id);
                      }}
                    />
                  </td>
                  <td className="p-2 border">{member.nickname}</td>
                  <td className="p-2 border">{member.bio}</td>
                  <td className="p-2 border">{member.role}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan={4}
                  className="p-4 text-center text-gray-400 border"
                >
                  등록된 멤버가 없습니다.
                </td>
              </tr>
            )}
          </tbody>
        </table>

        <div className="flex justify-center mt-4 space-x-2">
          {Array.from({ length: totalPages }).map((_, idx) => (
            <button
              key={idx}
              onClick={() => setPage(idx)}
              className={`px-3 py-1 rounded border ${
                page === idx
                  ? "bg-green-600 text-white"
                  : "bg-white hover:bg-gray-100"
              }`}
            >
              {idx + 1}
            </button>
          ))}
        </div>

        <div className="flex justify-end mt-6">
          <button
            onClick={onClose}
            className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded"
          >
            닫기
          </button>
        </div>

        {isRoleModalOpen && selectedMember && (
          <div className="fixed inset-0 z-60 flex items-center justify-center bg-black bg-opacity-30">
            <div className="bg-white rounded-xl shadow-lg p-6 w-80">
              <h3 className="text-lg font-semibold text-gray-800 mb-4">
                권한 선택
              </h3>
              <select
                value={selectedRole}
                onChange={(e) => setSelectedRole(e.target.value as RoleType)}
                className="w-full border rounded px-3 py-2 mb-4"
              >
                {selectableRoles.map((role) => (
                  <option key={role} value={role}>
                    {role}
                  </option>
                ))}
              </select>
              <div className="flex justify-end space-x-2">
                <button
                  onClick={() => setIsRoleModalOpen(false)}
                  className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded"
                >
                  취소
                </button>
                <button
                  onClick={handleRoleUpdate}
                  className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded"
                >
                  변경
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default MemberTableModal;
