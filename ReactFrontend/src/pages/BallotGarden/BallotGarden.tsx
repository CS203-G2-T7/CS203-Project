import { defaultGarden, Garden as GardenType } from "models/Garden";
import React, { useEffect, useState } from "react";
import Details from "./Details/Details";
import { BallotGardenStyled } from "./BallotGarden.styled";
import Header from "./Header/Header";
import Summary from "./Summary/Summary";
import { defaultWindow, Window } from "models/Window";
import { useParams } from "react-router-dom";
import { GarWin } from "models/GarWin";
import homeService from "service/homeService";
import { Ballot } from "models/Ballot";
import { AxiosError } from "axios";
import ballotService from "service/ballotService";
import { defaultGardenWin } from "models/GardenWin";
import dayjs from "dayjs";
import duration from "dayjs/plugin/duration";

export type DetailsType = {
  numPlots: number;
  leaseDate: string;
  window: string;
  status: string;
  availablePlots: number;
  ballotsPlaced: number;
};

const defaultDetails: DetailsType = {
  numPlots: 0,
  leaseDate: "",
  window: "",
  status: "",
  availablePlots: 0,
  ballotsPlaced: 0,
};

export default function BallotGarden() {
  const [latestWindow, setLatestWindow] = useState<Window>(defaultWindow);
  const [garWin, setGarWin] = useState<GarWin>(defaultGardenWin);
  const [garden, setGarden] = useState<GardenType>(defaultGarden);
  const [ballotArr, setBallotArr] = useState<Ballot[]>([]);
  let params = useParams();
  const gardenName: string =
    params.gardenName?.replace("-", " ") ?? "Garden Not Found";

  useEffect(() => {
    async function getAndSetData() {
      try {
        const latestWindow: Window = (await homeService.getLatestWindow()).data;
        setLatestWindow(latestWindow);
        const latestWinNumber: number = Number(
          latestWindow.windowId.substring(3)
        );

        const gardenLatestWinRelation: GarWin = (
          await ballotService.getGardenWinRelation(latestWinNumber, gardenName)
        ).data;
        setGarWin(gardenLatestWinRelation);
        const garden: GardenType = (
          await homeService.getGardenByName(gardenLatestWinRelation.sk)
        ).data;
        setGarden(garden);

        const ballotInGarWinArr: Ballot[] = (
          await homeService.getAllBallotsInWindGarden(
            latestWinNumber,
            gardenLatestWinRelation.sk
          )
        ).data;
        setBallotArr(ballotInGarWinArr);
      } catch (e) {
        if (
          e instanceof AxiosError &&
          e.message === "Request failed with status code 404"
        ) {
          console.log("There are no gardens found.");
        }
        console.log(e);
      }
    }

    getAndSetData();
  }, [gardenName]);

  dayjs.extend(duration);

  const winStartDate = dayjs(latestWindow.sk, "MM-DD-YYYY");
  const windowPeriod = dayjs.duration(latestWindow.windowDuration);
  const winEndDate = winStartDate.add(windowPeriod);
  const windowStartEnd =
    winStartDate.format("DD/MM/YYYY") + " - " + winEndDate.format("DD/MM/YYYY");

  const leaseEndDate = winEndDate.add(dayjs.duration("P3Y"));
  const leaseStartEnd =
    winEndDate.format("DD/MM/YYYY") + " - " + leaseEndDate.format("DD/MM/YYYY");

  const details: DetailsType = {
    numPlots: garden.numPlots,
    leaseDate: leaseStartEnd,
    window: windowStartEnd,
    availablePlots: garWin.numPlotsForBalloting,
    status: "Open",
    ballotsPlaced: ballotArr.length,
  };

  return (
    <BallotGardenStyled>
      <Header name={garden.sk} address={garden.gardenAddress} />
      <main>
        <Summary details={details} gardenName={garden.sk} winNum={Number(
          latestWindow.windowId.substring(3)
        )} />
        <Details />
      </main>
    </BallotGardenStyled>
  );
}
