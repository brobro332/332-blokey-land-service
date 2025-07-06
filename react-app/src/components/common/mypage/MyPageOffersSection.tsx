import React, { useCallback, useEffect, useState } from "react";
import { apiAxios } from "../../../utils/tsx/Api";
import { Offer } from "../../../types/offer";
import { useAuth } from "../../../App";

const MyPageOffersSection: React.FC = () => {
  const { blokey } = useAuth();

  const [receivedOffers, setReceivedOffers] = useState<Offer[]>([]);
  const [sentOffers, setSentOffers] = useState<Offer[]>([]);
  const [loading, setLoading] = useState(false);

  const [receivedPage, setReceivedPage] = useState(0);
  const [sentPage, setSentPage] = useState(0);

  const [receivedTotalPages, setReceivedTotalPages] = useState(0);
  const [sentTotalPages, setSentTotalPages] = useState(0);

  const fetchOffers = useCallback(
    async (offerer: "PROJECT" | "BLOKEY", page: number) => {
      setLoading(true);
      try {
        const res = await apiAxios("/blokey-land/api/offers", {
          method: "GET",
          params: {
            offerer,
            blokeyId: blokey?.id,
            page,
            size: 5,
          },
          withCredentials: true,
        });

        const data = res.data || res;

        if (offerer === "PROJECT") {
          setReceivedOffers(data.content || []);
          setReceivedTotalPages(data.totalPages || 0);
        } else {
          setSentOffers(data.content || []);
          setSentTotalPages(data.totalPages || 0);
        }
      } catch (e) {
        console.error("제안 목록 불러오기 실패", e);
      } finally {
        setLoading(false);
      }
    },
    [blokey?.id]
  );

  const patchOffer = async (
    offerId: number,
    action: "accept" | "reject" | "cancel"
  ) => {
    const methodMap = {
      accept: "PATCH",
      reject: "PATCH",
      cancel: "DELETE",
    };

    await apiAxios(`/blokey-land/api/offers/${offerId}`, {
      method: methodMap[action],
      data:
        action === "accept" || action === "reject"
          ? { status: action === "accept" ? "ACCEPTED" : "REJECTED" }
          : undefined,
      withCredentials: true,
    });

    if (action === "cancel") {
      setSentOffers((prev) => prev.filter((o) => o.id !== offerId));
    } else {
      setReceivedOffers((prev) =>
        prev.map((o) =>
          o.id === offerId
            ? { ...o, status: action === "accept" ? "ACCEPTED" : "REJECTED" }
            : o
        )
      );
    }
  };

  useEffect(() => {
    if (blokey?.id) {
      fetchOffers("PROJECT", receivedPage);
      fetchOffers("BLOKEY", sentPage);
    }
  }, [blokey, fetchOffers, receivedPage, sentPage]);

  const renderStatus = (status: string) => {
    if (status === "ACCEPTED") return "승낙";
    if (status === "REJECTED") return "거절";
    return "대기중";
  };

  return (
    <div className="flex flex-col h-[540px] space-y-8">
      <section className="flex-1 bg-green-50 rounded-xl p-4 overflow-auto">
        <h3 className="text-lg font-semibold mb-4">받은 제안</h3>

        <table className="w-full border text-sm">
          <thead className="bg-green-600 sticky top-0 text-white">
            <tr>
              <th className="px-4 py-2 border">프로젝트</th>
              <th className="px-4 py-2 border">상태</th>
            </tr>
          </thead>
          <tbody>
            {receivedOffers.length > 0 ? (
              receivedOffers.map((offer) => (
                <tr key={offer.id}>
                  <td className="px-4 py-2 border bg-white text-center">
                    {offer.projectTitle}
                  </td>
                  <td className="px-4 py-2 border bg-white text-center">
                    {offer.status === "PENDING" ? (
                      <div className="space-x-2">
                        <button
                          className="px-2 py-1 bg-green-500 text-white rounded text-sm"
                          onClick={() => patchOffer(offer.id, "accept")}
                        >
                          수락
                        </button>
                        <button
                          className="px-2 py-1 bg-red-500 text-white rounded text-sm"
                          onClick={() => patchOffer(offer.id, "reject")}
                        >
                          거절
                        </button>
                      </div>
                    ) : (
                      renderStatus(offer.status)
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan={2}
                  className="px-4 py-8 border bg-white text-center text-gray-500"
                >
                  받은 제안이 없습니다.
                </td>
              </tr>
            )}
          </tbody>
        </table>

        <div className="flex justify-center space-x-2 mt-4">
          <button
            disabled={receivedPage === 0 || loading}
            onClick={() => setReceivedPage(receivedPage - 1)}
            className="px-3 py-1 bg-gray-300 rounded disabled:opacity-50"
          >
            이전
          </button>
          <span className="px-3 py-1 select-none">
            {receivedPage + 1} / {receivedTotalPages}
          </span>
          <button
            disabled={receivedPage + 1 >= receivedTotalPages || loading}
            onClick={() => setReceivedPage(receivedPage + 1)}
            className="px-3 py-1 bg-gray-300 rounded disabled:opacity-50"
          >
            다음
          </button>
        </div>
      </section>

      <section className="flex-1 bg-green-50 rounded-xl p-4 overflow-auto">
        <h3 className="text-lg font-semibold mb-4">보낸 제안</h3>

        <table className="w-full border text-sm text-white">
          <thead className="bg-green-600 sticky top-0">
            <tr>
              <th className="px-4 py-2 border">프로젝트</th>
              <th className="px-4 py-2 border">상태</th>
            </tr>
          </thead>
          <tbody>
            {sentOffers.length > 0 ? (
              sentOffers.map((offer) => (
                <tr key={offer.id}>
                  <td className="px-4 py-2 bg-white border text-center text-black">
                    {offer.projectTitle}
                  </td>
                  <td className="px-4 py-2 bg-white border text-center text-black">
                    {offer.status === "PENDING" ? (
                      <button
                        className="px-2 py-1 bg-red-500 text-white rounded text-sm"
                        onClick={() => patchOffer(offer.id, "cancel")}
                      >
                        취소
                      </button>
                    ) : (
                      renderStatus(offer.status)
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan={2}
                  className="px-4 py-2 bg-white border text-center text-gray-500"
                >
                  보낸 제안이 없습니다.
                </td>
              </tr>
            )}
          </tbody>
        </table>

        <div className="flex justify-center space-x-2 mt-4">
          <button
            disabled={sentPage === 0 || loading}
            onClick={() => setSentPage(sentPage - 1)}
            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
          >
            이전
          </button>
          <span className="px-3 py-1 select-none">
            {sentPage + 1} / {sentTotalPages}
          </span>
          <button
            disabled={sentPage + 1 >= sentTotalPages || loading}
            onClick={() => setSentPage(sentPage + 1)}
            className="px-3 py-1 bg-gray-200 rounded disabled:opacity-50"
          >
            다음
          </button>
        </div>
      </section>
    </div>
  );
};

export default MyPageOffersSection;
