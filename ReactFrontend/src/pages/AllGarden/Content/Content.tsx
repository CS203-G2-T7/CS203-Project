import React, { useEffect, useState } from "react";
import Table from "./Table/Table";
import { ContentStyled } from "./Content.styled";
import allGardenService from "service/allgardenService";
import { defaultGarden, Garden } from "models/Garden";

import CircularProgress from "@mui/material/CircularProgress";
import { Box } from "@mui/material";

type Props = {};

export default function Content({}: Props) {
  const [allGardenDataList, setallGardenDataList] = useState<Garden[]>([
    defaultGarden,
  ]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([allGardenService.getAllGarden()])
      .then((resArr) => {
        console.log(resArr[0].data);
        setallGardenDataList(resArr[0].data);
        console.log(allGardenDataList);
        setLoading(false);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <ContentStyled>
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <CircularProgress />
        </Box>
      ) : (
        <Table gardenList={allGardenDataList ?? []} />
      )}
    </ContentStyled>
  );
}
