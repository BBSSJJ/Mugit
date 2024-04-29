"use client";

import { useCallback, useRef, useState } from "react";
import { useWavesurfer } from "@wavesurfer/react";
import IconPause from "@/app/assets/icon/IconPause";
import IconPlay from "../assets/icon/IconPlay";

const formatTime = (seconds: number) =>
  [seconds / 60, seconds % 60]
    .map((v) => `0${Math.floor(v)}`.slice(-2))
    .join(":");

// const formatTime = (seconds: number) => {
//   const minutes = Math.floor(seconds / 60);
//   const secondsRemainder = Math.round(seconds) % 60;
//   const paddedSeconds = `0${secondsRemainder}`.slice(-2);
//   return `${minutes}:${paddedSeconds}`;
// };

export default function WavesurferComp({ musicname }: { musicname: string }) {
  const audioUrl = `/musics/${musicname}`;
  const containerRef = useRef(null);
  const [duration, setDuration] = useState("0:00");

  const { wavesurfer, isPlaying, currentTime } = useWavesurfer({
    container: containerRef,
    height: 75,
    waveColor: "#f1f609",
    progressColor: "#0033ff",
    url: audioUrl,
    // plugins: useMemo(() => [Timeline.create()], []),
    barWidth: 2,
    dragToSeek: true,
    // barHeight: 0.75,
    // barGap: 1,
    // barRadius: 30,
  });

  const onPlayPause = useCallback(() => {
    wavesurfer && wavesurfer.playPause();
  }, [wavesurfer]);

  if (wavesurfer) {
    wavesurfer.on("decode", (duration) => setDuration(formatTime(duration)));
  }

  return (
    <div className="relative flex justify-between">
      <button
        onClick={onPlayPause}
        className="h-[75px] w-[75px] rounded-full bg-pointyellow"
      >
        {isPlaying ? (
          <IconPause
            color="#0033ff"
            size="3rem"
            tailwindCSS="absolute top-4 left-3.5"
          />
        ) : (
          <IconPlay
            color="#0033ff"
            size="3.2rem"
            tailwindCSS="absolute top-4 left-3.5"
          />
        )}
      </button>
      <div className="relative z-0 inline-block w-[88%]">
        <div ref={containerRef} />
        <div className="absolute bottom-0 left-0 z-10 -mt-[1px] bg-pointblack p-0.5 text-[11px] text-white">
          {formatTime(currentTime)}
        </div>
        <div className="absolute bottom-0 right-0 z-10 -mt-[1px] bg-pointblack p-0.5 text-[11px] text-white">
          {duration}
        </div>
      </div>
    </div>
  );
}