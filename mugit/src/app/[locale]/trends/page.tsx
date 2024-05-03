import Searchbar from "@/app/container/trends/searchbar";
import Trendslist from "@/app/container/trends/trendslist";
import { unstable_setRequestLocale } from "next-intl/server";

export default function TrendsPage({
  params: { locale },
}: {
  params: { locale: string };
}) {
  unstable_setRequestLocale(locale);
  return (
    <main
      //   className="py-10s flex min-h-[90%] w-full flex-auto flex-col content-center items-center
      // justify-center bg-pointyellow"
      className="flex min-h-[90%] w-full flex-auto flex-col items-center bg-pointyellow py-10
    "
    >
      <div className="flex h-full w-7/12 flex-col ">
        <Searchbar />
        <Trendslist />
      </div>
    </main>
  );
}
