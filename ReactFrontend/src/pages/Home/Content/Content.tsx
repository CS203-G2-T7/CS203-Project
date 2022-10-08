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

type Props = {};

export default function Content({}: Props) {
  //get latest window
  const [windowData, setWindowData] = useState<Window>(defaultWindow);
  const [latestWindowBallotList, setLatestWindowBallotList] = useState<
    Ballot[]
  >([defaultBallot]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      homeService.getLatestWindow(),
      homeService.getBallotsLatestWindow(),
    ])
      .then((resArr) => {
        console.log(resArr);
        setWindowData(resArr[0].data);
        setLatestWindowBallotList(resArr[1].data);
        setLoading(false);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [setWindowData, setLatestWindowBallotList]);

  //get formatted window date
  const windowDate: string =
    formatDateTimeToDate(windowData.startDateTime ?? "") +
    " - " +
    formatDateTimeToDate(windowData.leaseStart ?? "");

  return (
    <ContentStyled>
      <WindowLabel windowDate={windowDate} />
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <CircularProgress />
        </Box>
      ) : (
        <Table
          gardenList={windowData.gardenList ?? []}
          ballotList={latestWindowBallotList ?? []}
        />
      )}
    </ContentStyled>
  );
}
