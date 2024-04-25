"use client";
import { useState } from "react";
import DragDrop from "./DragDrop";
import AudioSprites from "../audioSprites/audiosprites";

export default function UploadContainer() {
  // 선택된 파일을 관리한다.
  const [file, setFile] = useState<File | null>(null);

  // 구현할 InputDragDrop에서 파일이 선택될 때 상태를 업데이트 한다.
  const handleFileSelect = (file: File | null) => {
    setFile(file);
    console.log(file);
  };

  // 파일 업로드를 처리하는 로직
  const handleUpload = () => {
    if (file) {
      // Drag & Drop으로 가져온 파일 처리 로직 (API 호출 등)
    }
  };

  return (
    <>
      {file ? (
        <>
          <AudioSprites mymedia={file} />
          <button
            onClick={() => setFile(null)}
            className="self-end rounded-md border-2 border-solid border-pointblue px-2 py-1 text-sm text-pointblue"
          >
            replace
          </button>
        </>
      ) : (
        <DragDrop
          onChangeFile={handleFileSelect}
          description="Drag and drop your file here"
          validExtensions={["mp3", "wav", "pcm", "wma"]} // 확장자 정보 추가
        ></DragDrop>
      )}
    </>
  );
}
