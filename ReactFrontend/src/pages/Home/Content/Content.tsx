import React, { useEffect, useState } from "react";
import Table from "./Table/Table";
import { ContentStyled } from "./Content.styled";
import WindowLabel from "./WindowLabel/WindowLabel";
import homeService from "service/homeService";
import { defaultWindow, Window } from "models/Window";
import { defaultGardenWin, GardenWin } from "models/GardenWin";
import formatDateTimeToDate from "utils/formatDateTimeToDate";
import { Ballot, defaultBallot } from "models/Ballot";
import CircularProgress from "@mui/material/CircularProgress";
import { Box } from "@mui/material";

type Props = {};

export default function Content({}: Props) {

  const [GardenWinList, setGardenWinList] = useState<
    GardenWin[]
  >([defaultGardenWin]);

  const [latestWindowBallotList, setLatestWindowBallotList] = useState<
    Ballot[]
  >([defaultBallot]);

  const [windowData, setWindowData] = useState<Window>(defaultWindow);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      homeService.getGardenInLatestWindow(),
      // homeService.getAllBallotsInWindGarden(),
      homeService.getLatestWindow(),
    ])
      .then((resArr) => {
        // console.log(resArr);
        // setWindowData(resArr[0].data);
        console.log(resArr[0].data);
        setGardenWinList(resArr[0].data);
        console.log(resArr[1].data);
        setLatestWindowBallotList([]);
        setWindowData(resArr[1].data);

        setLoading(false);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  //get formatted window date
  const windowDate: string =
    formatDateTimeToDate(windowData.startDateTime ?? "") +
    " - " +
    formatDateTimeToDate(windowData.leaseStart ?? "");

  return (
    <ContentStyled>
      <WindowLabel
        windowDate={windowDate.length <= 12 ? "Loading..." : windowDate}
      />
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <CircularProgress />
        </Box>
      ) : (
        <Table
          gardenWinList={GardenWinList ?? []}
          ballotList={latestWindowBallotList ?? []}
          windowData={windowData}
        />
      )}
    </ContentStyled>
  );
}
