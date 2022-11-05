import React, { useEffect, useState } from "react";
import Table from "./Table/Table";
import { ContentStyled } from "./Content.styled";
import WindowLabel from "./WindowLabel/WindowLabel";
import homeService from "service/homeService";
import { defaultWindow, Window } from "models/Window";
import formatDateTimeToDate from "utils/formatDateTimeToDate";
import { Ballot, defaultBallot } from "models/Ballot";
import CircularProgress from "@mui/material/CircularProgress";
import { Box } from "@mui/material";
import { AxiosError } from "axios";
import { Garden } from "models/Garden";
import { GarWin } from "models/GarWin";

type Props = {};

export type Row = {
  gardenName: string;
  gardenAddress: string;
  availablePlots: number;
  ballotsPlaced: number;
  status: "closed" | "open";
};

export const defaultRow: Row = {
  gardenName: "",
  gardenAddress: "",
  availablePlots: 0,
  ballotsPlaced: 0,
  status: "closed",
};

export default function Content({}: Props) {
  //get latest window
  const [rowData, setRowData] = useState<Row[]>([]);
  const [windowData, setWindowData] = useState<Window>(defaultWindow);
  const [loading, setLoading] = useState(true);

  async function getAndSetData() {
    try {
      const latestWindow: Window = (await homeService.getLatestWindow()).data;
      setWindowData(latestWindow);
      const latestWinNumber: number = Number(
        latestWindow.windowId.substring(3)
      );

      const latestGardenArr: GarWin[] = (
        await homeService.getGardenInLatestWindow(latestWinNumber)
      ).data;

      await latestGardenArr.forEach(async (garWinRelation) => {
        const garden: Garden = (
          await homeService.getGardenByName(garWinRelation.sk)
        ).data;

        const ballotInGarWinArr: Ballot[] = (
          await homeService.getAllBallotsInWindGarden(
            latestWinNumber,
            garWinRelation.sk
          )
        ).data;

        setRowData((prevRowData) => {
          const newRowData: Row[] = prevRowData.slice(0);
          const newRow: Row = {
            gardenName: garden.sk,
            gardenAddress: garden.gardenAddress,
            availablePlots: garWinRelation.numPlotsForBalloting,
            ballotsPlaced: ballotInGarWinArr.length,
            status: "open",
          };
          newRowData.push(newRow);
          return newRowData;
        });
      });
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

  useEffect(() => {
    getAndSetData();
  }, []);

  return (
    <ContentStyled>
      <WindowLabel windowData={windowData} />
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <CircularProgress />
        </Box>
      ) : (
        <Table rowList={rowData} />
      )}
    </ContentStyled>
  );
}
