import { Garden, Garden as GardenType } from "models/Garden";
import React, { useEffect, useState } from "react";
import { useLocation } from "react-router";
import Details from "./Details/Details";
import { BallotGardenStyled } from "./BallotGarden.styled";
import Header from "./Header/Header";
import Summary from "./Summary/Summary";
import { defaultWindow, Window } from "models/Window";
import { useParams } from "react-router-dom";
import { Row } from "pages/BallotGardenList/Content/Content";
import { GarWin } from "models/GarWin";
import homeService from "service/homeService";
import { Ballot } from "models/Ballot";
import { AxiosError } from "axios";
import ballotService from "service/ballotService";

type Props = {};

export type LinkStateType = {
  gardenObject: GardenType;
  numBallotsPlaced: number;
  windowData: Window;
};

export default function BallotGarden({}: Props) {
  const [rowData, setRowData] = useState<Row[]>([]);
  const [windowData, setWindowData] = useState<Window>(defaultWindow);
  const [loading, setLoading] = useState(true);
  let params = useParams();
  const gardenName: string =
    params.gardenName?.replace("-", " ") ?? "Garden Not Found";

  

  useEffect(() => {
    async function getAndSetData() {
      try {
        const latestWindow: Window = (await homeService.getLatestWindow()).data;
        setWindowData(latestWindow);
        const latestWinNumber: number = Number(
          latestWindow.windowId.substring(3)
        );
        console.log(latestWindow);
  
        const gardenLatestWinRelation: GarWin = (
          await ballotService.getGardenWinRelation(latestWinNumber, gardenName)
        ).data;
  
        console.log(gardenLatestWinRelation);
  
        const garden: Garden = (
          await homeService.getGardenByName(gardenLatestWinRelation.sk)
        ).data;

        console.log(garden);
  
        const ballotInGarWinArr: Ballot[] = (
          await homeService.getAllBallotsInWindGarden(
            latestWinNumber,
            gardenLatestWinRelation.sk
          )
        ).data;

        console.log(ballotInGarWinArr);
  
        setLoading(false);
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

  return (
    <BallotGardenStyled>
      {/* <Header
        name={linkState.gardenObject.name}
        address={linkState.gardenObject.location}
      /> */}
      <main>
        <Summary />
        <Details />
      </main>
    </BallotGardenStyled>
  );
}
