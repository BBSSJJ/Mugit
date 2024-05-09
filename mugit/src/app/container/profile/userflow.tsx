"use client";

import { useLocale, useTranslations } from "next-intl";
import { useParams, useRouter } from "next/navigation";
import { userAtom } from "@/app/store/atoms/user";
import { useAtomValue } from "jotai";
import { Tab } from "@headlessui/react";
import Image from "next/image";
import dynamic from "next/dynamic";
import { useEffect, useState } from "react";
import { apiUrl } from "@/app/store/atoms";
import { FlowType } from "@/app/types/flowtype";
const WavesurferComp = dynamic(() => import("@/app/components/wavesurfer"), {
  ssr: false,
});

const fetchFlows = async (id: string, type: string) => {
  const response = await fetch(apiUrl + `/flows/users/${id}/${type}`, {
    credentials: "include",
  });
  return response.json();
};

export default function UserFlow() {
  const params = useParams();
  const t = useTranslations("Flow");
  const locale = useLocale();
  const router = useRouter();
  const user = useAtomValue(userAtom);
  const [flows, setFlows] = useState([]);
  const [likes, setLikes] = useState([]);
  const [works, setWorks] = useState([]);
  useEffect(() => {
    fetchFlows(String(params.id), "released").then((data) =>
      setFlows(data.list)
    );
    fetchFlows(String(params.id), "likes").then((data) => setLikes(data.list));
    if (params.id === user.id) {
      fetchFlows(String(params.id), "unreleased").then((data) =>
        setWorks(data.list)
      );
    }
  }, []);

  return (
    <div className="relative mx-auto my-10 w-2/3">
      <Tab.Group>
        <Tab.List>
          <Tab
            className={({ selected }) =>
              (selected
                ? "font-bold underline decoration-4 underline-offset-[7.3px] focus:outline-none "
                : "") + "pr-5 text-2xl"
            }
          >
            Flows
          </Tab>
          <Tab
            className={({ selected }) =>
              (selected
                ? "font-bold underline decoration-4 underline-offset-[7.3px] focus:outline-none "
                : "") + "pr-5 text-2xl"
            }
          >
            Likes
          </Tab>
          {params.id === user.id ? (
            <Tab
              className={({ selected }) =>
                (selected
                  ? "font-bold underline decoration-4 underline-offset-[7.3px] focus:outline-none "
                  : "") + "pr-5 text-2xl"
              }
            >
              Works
            </Tab>
          ) : (
            <></>
          )}
          <button
            className="absolute -top-1.5 right-0 rounded border-2 border-pointblue bg-pointblue px-2 
            py-[1px] text-xl text-white transition duration-300 hover:bg-[#0831d6]"
            onClick={() => router.push(`/${locale}/note`)}
          >
            {t("newNote")}
          </button>
        </Tab.List>
        <hr className="border-2" />
        <Tab.Panels>
          <Tab.Panel>
            {flows.map((flow: FlowType) => (
              <div key={flow.id} className="my-5 flex w-full">
                <Image src={flow.coverPath} alt="" width={150} height={150} />
                <div className="relative ml-5 w-full">
                  <p className="text-xl font-semibold">{flow.title}</p>
                  <p className="text-base">{flow.user.nickName}</p>
                  <div className="absolute bottom-0 w-full">
                    <WavesurferComp
                      musicPath={flow.musicPath}
                      musicname={flow.title}
                      type="source"
                    />
                  </div>
                </div>
              </div>
            ))}
          </Tab.Panel>
          <Tab.Panel>
            {likes.map((flow: FlowType) => (
              <div key={flow.id} className="my-5 flex w-full">
                <Image src={flow.coverPath} alt="" width={150} height={150} />
                <div className="relative ml-5 w-full">
                  <p className="text-xl font-semibold">{flow.title}</p>
                  <p className="text-base">{flow.user.nickName}</p>
                  <div className="absolute bottom-0 w-full">
                    <WavesurferComp
                      musicPath={flow.musicPath}
                      musicname={flow.title}
                      type="source"
                    />
                  </div>
                </div>
              </div>
            ))}
          </Tab.Panel>
          {params.id === user.id ? (
            <Tab.Panel>
              {works.map((flow: FlowType) => (
                <div key={flow.id} className="my-5 flex w-full">
                  <Image src={flow.coverPath} alt="" width={150} height={150} />
                  <div className="relative ml-5 w-full">
                    <p className="text-xl font-semibold">{flow.title}</p>
                    <p className="text-base">{flow.user.nickName}</p>
                    <div className="absolute bottom-0 w-full">
                      <WavesurferComp
                        musicPath={flow.musicPath}
                        musicname={flow.title}
                        type="source"
                      />
                    </div>
                  </div>
                </div>
              ))}
            </Tab.Panel>
          ) : (
            <></>
          )}
        </Tab.Panels>
      </Tab.Group>
    </div>
  );
}
