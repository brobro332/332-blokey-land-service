import React, { useState, useEffect } from "react";
import { FaStar } from "react-icons/fa";
import defaultProjectImage from "../../../assets/image/default-project.png";
import { apiAxios } from "../../../utils/tsx/Api";
import { Project } from "../../../types/project";

interface ProjectFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  mode: "create" | "edit";
  initialData?: {
    id: number;
    title: string;
    description: string;
    estimatedStartDate: string;
    estimatedEndDate: string;
    actualStartDate: string;
    actualEndDate: string;
    imageUrl: string;
    isPrivate: "public" | "private";
  };
  onCreate?: (
    title: string,
    description: string,
    imageUrl: string,
    isPrivate: "public" | "private",
    estimatedStartDate: string,
    estimatedEndDate: string,
    actualStartDate: string,
    actualEndDate: string
  ) => Promise<void>;
  onUpdate?: (project: Project) => Promise<void>;
}

const ProjectFormModal: React.FC<ProjectFormModalProps> = ({
  isOpen,
  onClose,
  mode,
  initialData,
  onCreate,
  onUpdate,
}) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(null);
  const [isPrivate, setIsPrivate] = useState<"public" | "private">("public");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [estimatedStartDate, setEstimatedStartDate] = useState("");
  const [estimatedEndDate, setEstimatedEndDate] = useState("");
  const [actualStartDate, setActualStartDate] = useState("");
  const [actualEndDate, setActualEndDate] = useState("");

  /**
   * @description 이미지 변경
   */
  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0] || null;
    setImageFile(file);
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => setImagePreview(reader.result as string);
      reader.readAsDataURL(file);
    } else {
      setImagePreview(null);
    }
  };

  /**
   * @description 이미지 업로드
   */
  const uploadImage = async (file: File): Promise<string> => {
    const formData = new FormData();
    formData.append("file", file);

    const data = await apiAxios<string>("/blokey-land/api/files", {
      method: "POST",
      data: formData,
      headers: undefined,
    });

    return data;
  };

  /**
   * @description 입력 폼 제출
   */
  const handleSubmit = async () => {
    if (title.trim() === "") {
      alert("프로젝트 제목을 입력해주세요.");
      return;
    }

    setIsSubmitting(true);

    try {
      let imageUrl = imagePreview || "";
      if (imageFile) {
        imageUrl = await uploadImage(imageFile);
      }

      if (onCreate && mode === "create") {
        onCreate(
          title,
          description,
          imageUrl,
          isPrivate,
          estimatedStartDate,
          estimatedEndDate,
          actualStartDate,
          actualEndDate
        );
      } else if (onUpdate && mode === "edit" && initialData) {
        onUpdate({
          id: initialData.id,
          title,
          description,
          imageUrl,
          isPrivate,
          estimatedStartDate,
          estimatedEndDate,
          actualStartDate,
          actualEndDate,
        });
      }

      onClose();
    } catch (error) {
      alert((error as Error).message || "오류가 발생했습니다.");
    } finally {
      setIsSubmitting(false);
    }
  };

  /**
   * @description 입력 폼 초기화
   */
  useEffect(() => {
    if (mode === "edit" && initialData) {
      setTitle(initialData.title);
      setDescription(initialData.description);
      setEstimatedStartDate(initialData.estimatedStartDate);
      setEstimatedEndDate(initialData.estimatedEndDate);
      setActualStartDate(initialData.actualStartDate);
      setActualEndDate(initialData.actualEndDate);
      setIsPrivate(initialData.isPrivate ? "private" : "public");
      setImagePreview(initialData.imageUrl || null);
      setImageFile(null);
    } else if (mode === "create") {
      setTitle("");
      setDescription("");
      setEstimatedStartDate("");
      setEstimatedEndDate("");
      setActualStartDate("");
      setActualEndDate("");
      setIsPrivate("public");
      setImagePreview(null);
      setImageFile(null);
    }
  }, [mode, initialData, isOpen]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 pb-2 w-[720px] max-h-[730px] shadow-lg overflow-auto">
        <h2 className="text-xl font-bold mb-2 border-b-2 border-green-400 text-green-700">
          {mode === "create" ? "새 프로젝트 만들기" : "프로젝트 수정하기"}
        </h2>

        <div className="grid grid-cols-2 gap-2 bg-gray-100 rounded-xl p-4 shadow-sm">
          <div className="col-span-2">
            <label className="inline-flex text-sm font-medium mb-1">
              제목
              {
                FaStar({
                  size: 10,
                  className: "text-red-500 ml-1",
                }) as React.ReactElement
              }
            </label>
            <input
              type="text"
              placeholder="프로젝트 제목"
              className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              disabled={isSubmitting}
            />
          </div>

          <div className="col-span-2">
            <label className="block text-sm font-medium mb-1">설명</label>
            <textarea
              placeholder="프로젝트 설명"
              className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400 resize-none"
              rows={2}
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              disabled={isSubmitting}
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">시작예정일</label>
            <input
              type="date"
              className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
              value={estimatedStartDate}
              onChange={(e) => setEstimatedStartDate(e.target.value)}
              disabled={isSubmitting}
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">종료예정일</label>
            <input
              type="date"
              className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
              value={estimatedEndDate}
              onChange={(e) => setEstimatedEndDate(e.target.value)}
              disabled={isSubmitting}
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">실제시작일</label>
            <input
              type="date"
              className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
              value={actualStartDate}
              onChange={(e) => setActualStartDate(e.target.value)}
              disabled={isSubmitting}
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">실제종료일</label>
            <input
              type="date"
              className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-green-400"
              value={actualEndDate}
              onChange={(e) => setActualEndDate(e.target.value)}
              disabled={isSubmitting}
            />
          </div>

          <div className="space-y-3">
            <div className="mb-4">
              <label className="block text-sm font-medium mb-1">
                이미지 업로드
                {(imagePreview || imageFile) && (
                  <button
                    type="button"
                    onClick={() => {
                      setImageFile(null);
                      setImagePreview(null);
                    }}
                    className="text-xs text-red-600 hover:underline ml-2"
                    disabled={isSubmitting}
                  >
                    이미지 삭제
                  </button>
                )}
              </label>
              <input
                type="file"
                accept="image/*"
                onChange={handleImageChange}
                className="block bg-white rounded-3xl w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4
                  file:rounded-full file:border-0 file:text-sm file:font-semibold
                  file:bg-green-50 file:text-green-700 hover:file:bg-green-100"
                disabled={isSubmitting}
              />
            </div>

            <div>
              <label className="inline-flex text-sm font-medium mb-1">
                공개 여부
                {
                  FaStar({
                    size: 10,
                    className: "text-red-500 ml-1",
                  }) as React.ReactElement
                }
              </label>
              <div className="flex space-x-4">
                <label className="inline-flex items-center space-x-2">
                  <input
                    type="radio"
                    name="isPrivate"
                    value="public"
                    checked={isPrivate === "public"}
                    onChange={() => setIsPrivate("public")}
                    disabled={isSubmitting}
                  />
                  <span>공개</span>
                </label>
                <label className="inline-flex items-center space-x-2">
                  <input
                    type="radio"
                    name="isPrivate"
                    value="private"
                    checked={isPrivate === "private"}
                    onChange={() => setIsPrivate("private")}
                    disabled={isSubmitting}
                  />
                  <span>비공개</span>
                </label>
              </div>
            </div>
          </div>

          <div className="relative flex flex-col items-center justify-center border rounded border-gray-300 p-2 min-h-[12rem] bg-white space-y-2">
            <img
              src={
                imageFile
                  ? imagePreview ?? defaultProjectImage
                  : imagePreview
                  ? `http://localhost:8080/blokey-land/app/uploads/${imagePreview}`
                  : defaultProjectImage
              }
              alt="이미지 미리보기"
              className="max-h-44 object-contain w-full"
            />
            {!imagePreview && (
              <div className="absolute inset-0 flex items-center justify-center bg-white bg-opacity-70 text-gray-500 text-sm italic pointer-events-none">
                이미지가 없습니다.
              </div>
            )}
          </div>
        </div>

        <div className="flex justify-end space-x-2 mt-2">
          <button
            className="px-4 py-2 rounded bg-gray-300 hover:bg-gray-400"
            onClick={() => {
              setTitle("");
              setDescription("");
              setEstimatedStartDate("");
              setEstimatedEndDate("");
              setActualStartDate("");
              setActualEndDate("");
              setImageFile(null);
              setImagePreview(null);
              setIsPrivate("public");
              onClose();
            }}
            disabled={isSubmitting}
          >
            취소
          </button>
          <button
            className="px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700"
            onClick={handleSubmit}
            disabled={isSubmitting}
          >
            {isSubmitting
              ? mode === "create"
                ? "생성 중..."
                : "수정 중..."
              : mode === "create"
              ? "생성"
              : "수정"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProjectFormModal;
