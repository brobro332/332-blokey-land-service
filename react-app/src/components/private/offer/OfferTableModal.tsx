import React, { useCallback, useEffect, useState } from "react";
import { Offer } from "../../../types/offer";
import { Member } from "../../../types/member";
import { Blokey } from "../../../types/blokey";
import { apiAxios } from "../../../utils/tsx/Api";

interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  first: boolean;
  last: boolean;
}

interface OfferTableModalProps {
  selectedProjectId: number | undefined;
}

const PAGE_SIZE = 10;

const OfferTableModal: React.FC<OfferTableModalProps> = ({
  selectedProjectId,
}) => {
  const [offers, setOffers] = useState<Offer[]>([]);
  const [tab, setTab] = useState<"received" | "sent" | "invite">("received");
  const [blokeysPage, setBlokeysPage] = useState<Page<Blokey>>({
    content: [],
    totalElements: 0,
    totalPages: 1,
    number: 0,
    first: true,
    last: true,
  });
  const [members, setMembers] = useState<Member[]>([]);
  const [blokeysPageNumber, setBlokeysPageNumber] = useState(0);
  const [keywordFilter, setKeywordFilter] = useState({
    received: "",
    sent: "",
    invite: "",
  });
  const [statusFilter, setStatusFilter] = useState({
    received: "",
    sent: "",
    invite: "",
  });
  const [receivedPage, setReceivedPage] = useState(0);
  const [receivedTotalPages, setReceivedTotalPages] = useState(1);
  const [sentPage, setSentPage] = useState(0);
  const [sentTotalPages, setSentTotalPages] = useState(1);

  const participatingBlokeyIds = members.map((m) => m.blokeyId);
  const nonMemberBlokeys = blokeysPage.content.filter(
    (b) => !participatingBlokeyIds.includes(b.id)
  );

  /**
   * @description 제안 필터링
   */
  const filterOffers = (offers: Offer[], type: "BLOKEY" | "PROJECT") => {
    return offers.filter(
      (o) => o.offerer === type && o.projectId === selectedProjectId
    );
  };

  const filteredReceived = filterOffers(offers, "BLOKEY").filter((o) => {
    const kw = keywordFilter.received.toLowerCase();
    const matchKeyword =
      o.blokeyNickname.toLowerCase().includes(kw) ||
      o.blokeyBio.toLowerCase().includes(kw);
    const matchStatus =
      !statusFilter.received ||
      o.status.toLowerCase() === statusFilter.received.toLowerCase();
    return matchKeyword && matchStatus;
  });

  const filteredSent = filterOffers(offers, "PROJECT").filter((o) => {
    const kw = keywordFilter.sent.toLowerCase();
    const matchKeyword =
      o.blokeyNickname.toLowerCase().includes(kw) ||
      o.blokeyBio.toLowerCase().includes(kw);
    const matchStatus =
      !statusFilter.sent ||
      o.status.toLowerCase() === statusFilter.sent.toLowerCase();
    return matchKeyword && matchStatus;
  });

  const filteredInvite = nonMemberBlokeys.filter((b) => {
    const kw = keywordFilter.invite.toLowerCase();
    const matchKeyword =
      b.nickname.toLowerCase().includes(kw) || b.bio.toLowerCase().includes(kw);

    const blokeyOffers = offers.filter(
      (o) => o.projectId === selectedProjectId && o.blokeyId === b.id
    );
    const hasPending = blokeyOffers.some((o) => o.status === "PENDING");

    const matchStatus =
      !statusFilter.invite || (statusFilter.invite === "PENDING" && hasPending);

    return matchKeyword && matchStatus;
  });

  /**
   * @description 제안 생성
   */
  const createOffer = async (blokeyId: string) => {
    if (!selectedProjectId) return;
    try {
      await apiAxios<Offer>("/blokey-land/api/offers", {
        method: "POST",
        data: {
          blokeyId,
          projectId: selectedProjectId,
          offerer: "PROJECT",
          status: "PENDING",
        },
      });
      fetchOffers("PROJECT", receivedPage);
      fetchBlokeys();
    } catch (e) {
      alert("제안 전송 중 오류가 발생했습니다.");
    }
  };

  /**
   * @description 제안 조회
   */
  const fetchOffers = useCallback(
    async (offerer: string, page: number) => {
      if (!selectedProjectId) return;
      try {
        const res = await apiAxios<Page<Offer>>(
          `/blokey-land/api/offers?projectId=${selectedProjectId}&offerer=${offerer}&page=${page}&size=${PAGE_SIZE}`
        );
        setOffers(res.content || []);
        if (offerer === "BLOKEY") setReceivedTotalPages(res.totalPages);
        else if (offerer === "PROJECT") setSentTotalPages(res.totalPages);
      } catch (e) {
        console.error("DTO 제안 불러오기 실패", e);
        setOffers([]);
      }
    },
    [selectedProjectId]
  );

  /**
   * @description 제안 삭제
   */
  const deleteOffer = async (offerId: number) => {
    try {
      await apiAxios(`/blokey-land/api/offers/${offerId}`, {
        method: "DELETE",
      });
      if (tab === "received") fetchOffers("BLOKEY", receivedPage);
      else if (tab === "sent") fetchOffers("PROJECT", sentPage);
      fetchBlokeys();
    } catch (e) {
      alert("제안 취소 중 오류가 발생했습니다.");
    }
  };

  /**
   * @description 제안 목록 초기화
   */
  useEffect(() => {
    if (!selectedProjectId) return;

    if (tab === "received") {
      fetchOffers("BLOKEY", receivedPage);
    } else if (tab === "sent") {
      fetchOffers("PROJECT", sentPage);
    } else {
      setOffers([]);
    }
  }, [selectedProjectId, tab, receivedPage, sentPage, fetchOffers]);

  /**
   * @description 사용자 조회
   */
  const fetchBlokeys = useCallback(async () => {
    if (!selectedProjectId) return;

    try {
      const data = await apiAxios<Page<Blokey>>(
        `/blokey-land/api/blokeys?excludeProjectId=${selectedProjectId}&page=${blokeysPageNumber}&size=${PAGE_SIZE}`
      );
      setBlokeysPage(data);
    } catch {
      setBlokeysPage({
        content: [],
        totalElements: 0,
        totalPages: 1,
        number: 0,
        first: true,
        last: true,
      });
    }
  }, [blokeysPageNumber, selectedProjectId]);

  /**
   * @description 사용자 목록 초기화
   */
  useEffect(() => {
    fetchBlokeys();
  }, [selectedProjectId, blokeysPageNumber, fetchBlokeys]);

  /**
   * @description 멤버 목록 초기화
   */
  useEffect(() => {
    if (!selectedProjectId) {
      setMembers([]);
      return;
    }
    apiAxios<Page<Member>>(
      `/blokey-land/api/projects/${selectedProjectId}/members?page=0&size=10`
    )
      .then((res) => setMembers(res.content))
      .catch(() => {});
  }, [selectedProjectId]);

  return (
    <div className="bg-white p-4 rounded-xl shadow-md w-full max-w-4xl mx-auto min-h-[500px]">
      {!selectedProjectId ? (
        <p className="text-gray-600">프로젝트를 선택해주세요.</p>
      ) : (
        <>
          <div className="flex mb-4 space-x-4">
            {["received", "sent", "invite"].map((key) => (
              <button
                key={key}
                className={`px-4 py-2 rounded font-semibold text-sm ${
                  tab === key
                    ? "bg-blue-600 text-white"
                    : "bg-gray-200 text-gray-700"
                }`}
                onClick={() => {
                  setTab(key as any);
                  if (key === "received") setReceivedPage(0);
                  else if (key === "sent") setSentPage(0);
                }}
              >
                {key === "received"
                  ? "받은 제안"
                  : key === "sent"
                  ? "보낸 제안"
                  : "사용자 목록"}
              </button>
            ))}
          </div>

          <div className="mb-4 flex flex-wrap items-center gap-4">
            <input
              type="text"
              placeholder="닉네임 또는 소개"
              value={keywordFilter[tab]}
              onChange={(e) =>
                setKeywordFilter((prev) => ({ ...prev, [tab]: e.target.value }))
              }
              className="border px-2 py-1 rounded w-60"
            />
            <select
              value={statusFilter[tab]}
              onChange={(e) =>
                setStatusFilter((prev) => ({ ...prev, [tab]: e.target.value }))
              }
              className="border px-2 py-1 rounded"
            >
              <option value="">전체 상태</option>
              <option value="PENDING">대기중</option>
              {(tab === "received" || tab === "sent") && (
                <>
                  <option value="ACCEPTED">수락됨</option>
                  <option value="REJECTED">거절됨</option>
                </>
              )}
            </select>
          </div>

          {(tab === "received" || tab === "sent") && (
            <table className="w-full table-auto border">
              <thead>
                <tr>
                  <th className="border px-4 py-2">제안일자</th>
                  <th className="border px-4 py-2">닉네임</th>
                  <th className="border px-4 py-2">자기소개</th>
                  <th className="border px-4 py-2">상태</th>
                </tr>
              </thead>
              <tbody>
                {(tab === "received" ? filteredReceived : filteredSent)
                  .length === 0 ? (
                  <tr>
                    <td colSpan={4} className="text-center py-4 text-gray-500">
                      제안이 없습니다.
                    </td>
                  </tr>
                ) : (
                  (tab === "received" ? filteredReceived : filteredSent).map(
                    (o) => {
                      return (
                        <tr key={o.id}>
                          <td className="border px-4 py-2">
                            {new Date(o.createdAt).toLocaleDateString()}
                          </td>
                          <td className="border px-4 py-2">
                            {o.blokeyNickname}
                          </td>
                          <td className="border px-4 py-2">{o.blokeyBio}</td>
                          <td className="border px-4 py-2 text-center">
                            {o.status === "PENDING" ? (
                              <button
                                onClick={() => deleteOffer(o.id)}
                                className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 text-sm"
                              >
                                제안 취소
                              </button>
                            ) : (
                              o.status
                            )}
                          </td>
                        </tr>
                      );
                    }
                  )
                )}
              </tbody>
            </table>
          )}

          {(tab === "received" || tab === "sent") && (
            <div className="flex justify-center items-center space-x-4 mt-4">
              <button
                onClick={() => {
                  if (tab === "received")
                    setReceivedPage((prev) => Math.max(prev - 1, 0));
                  else if (tab === "sent")
                    setSentPage((prev) => Math.max(prev - 1, 0));
                }}
                disabled={
                  (tab === "received" && receivedPage === 0) ||
                  (tab === "sent" && sentPage === 0)
                }
                className="px-3 py-1 rounded border text-sm"
              >
                이전
              </button>
              <span className="text-sm">
                {(tab === "received" ? receivedPage : sentPage) + 1} /{" "}
                {tab === "received"
                  ? receivedTotalPages
                  : tab === "sent"
                  ? sentTotalPages
                  : 1}
              </span>
              <button
                onClick={() => {
                  if (tab === "received")
                    setReceivedPage((prev) =>
                      Math.min(prev + 1, receivedTotalPages - 1)
                    );
                  else if (tab === "sent")
                    setSentPage((prev) =>
                      Math.min(prev + 1, sentTotalPages - 1)
                    );
                }}
                disabled={
                  (tab === "received" &&
                    receivedPage >= receivedTotalPages - 1) ||
                  (tab === "sent" && sentPage >= sentTotalPages - 1)
                }
                className="px-3 py-1 rounded border text-sm"
              >
                다음
              </button>
            </div>
          )}

          {tab === "invite" && (
            <>
              <table className="w-full table-auto border">
                <thead>
                  <tr>
                    <th className="border px-4 py-2">닉네임</th>
                    <th className="border px-4 py-2">자기소개</th>
                    <th className="border px-4 py-2">상태</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredInvite.length === 0 ? (
                    <tr>
                      <td
                        colSpan={3}
                        className="text-center py-4 text-gray-500"
                      >
                        조회된 블로키가 없습니다.
                      </td>
                    </tr>
                  ) : (
                    filteredInvite.map((blokey) => {
                      return (
                        <tr key={blokey.id}>
                          <td className="border px-4 py-2">
                            {blokey.nickname}
                          </td>
                          <td className="border px-4 py-2">{blokey.bio}</td>
                          <td className="border px-4 py-2 text-center">
                            {blokey.hasPendingOffer ? (
                              "전송됨"
                            ) : (
                              <button
                                onClick={() => createOffer(blokey.id)}
                                className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                              >
                                제안 보내기
                              </button>
                            )}
                          </td>
                        </tr>
                      );
                    })
                  )}
                </tbody>
              </table>

              <div className="flex justify-center items-center space-x-4 mt-4">
                <button
                  onClick={() =>
                    setBlokeysPageNumber((prev) => Math.max(prev - 1, 0))
                  }
                  disabled={blokeysPage.first}
                  className="px-3 py-1 rounded border text-sm"
                >
                  이전
                </button>
                <span className="text-sm">
                  {blokeysPage.number + 1} / {blokeysPage.totalPages}
                </span>
                <button
                  onClick={() =>
                    setBlokeysPageNumber((prev) =>
                      Math.min(prev + 1, blokeysPage.totalPages - 1)
                    )
                  }
                  disabled={blokeysPage.last}
                  className="px-3 py-1 rounded border text-sm"
                >
                  다음
                </button>
              </div>
            </>
          )}
        </>
      )}
    </div>
  );
};

export default OfferTableModal;
