"use client";
import Description from "@/app/components/Description";
import UploadPicture from "@/app/components/fileUpload/UploadPicture";
import RecordMessage from "@/app/container/flow/release/recordMessage";
import dynamic from "next/dynamic";
import { useState } from "react";
import { useInput } from "@/app/hooks/useInput";
import { StaticImport } from "next/dist/shared/lib/get-img-props";
import SelectTags from "@/app/components/selectTags";

const WavesurferComp = dynamic(() => import("@/app/components/wavesurfer"), {
  ssr: false,
});

export default function NotePage() {
  const [name, handleChangeName] = useInput("");
  const [description, handleChangeDescription] = useInput("");
  const [imageSrc, setImageSrc] = useState<string | StaticImport>(
    "/person.jpg"
  );
  return (
    <main className="relative flex min-h-[90%] w-full flex-col px-52 py-10">
      <h1 className="relative border-b-2 border-solid border-gray-300 pl-5 text-5xl font-bold italic">
        Release Note
      </h1>
      <Description target="release" />

      <div className="mt-4 flex w-full">
        {/* 사진 올리는 부분임 */}
        <UploadPicture imageSrc={imageSrc} setImageSrc={setImageSrc} />

        <div className="flex w-9/12 flex-col">
          <h2 className=" text-lg">Note Name</h2>
          <input
            type="text"
            className="h-8 w-full rounded-lg border-2 border-solid border-gray-300 border-b-gray-200 px-4"
          />

          <h2 className="mt-4 text-lg">Flow Name </h2>
          <input
            value={name}
            onChange={handleChangeName}
            type="text"
            className="h-8 w-full rounded-lg border-2 border-solid border-gray-300 border-b-gray-200 px-4"
          />

          <SelectTags />

          <WavesurferComp musicname="Burkinelectric.mp3" />

          <h2 className="mt-6 text-lg">Record Messages</h2>
          <RecordMessage />

          <h2 className="mt-4 text-lg">Description</h2>
          <textarea
            value={description}
            onChange={handleChangeDescription}
            placeholder="Describe your Note"
            className="h-52 w-full rounded-lg border-2 border-solid border-gray-300 border-b-gray-200 p-4"
          />

          <div className="mt-5 flex w-full justify-end gap-x-3">
            <button className="rounded-lg border-2 border-solid bg-gray-100 px-10 py-3 text-gray-600">
              Cancel
            </button>
            <button className="rounded-lg bg-pointblue px-10 py-3 text-white">
              Save
            </button>
          </div>
        </div>
      </div>
    </main>
  );
}