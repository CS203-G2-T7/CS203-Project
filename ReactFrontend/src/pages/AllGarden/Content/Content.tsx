import React, { useEffect, useState } from "react";
import { ContentStyled } from "./Content.styled";
import allGardenService from "service/allgardenService";
import { defaultGarden, Garden } from "models/Garden";
import WindowLabel from "./WindowLabel/WindowLabel";

import CircularProgress from "@mui/material/CircularProgress";
import { Box } from "@mui/material";
import HeaderRow from "./Table/HeaderRow/HeaderRow";
import Row from "./Table/Row";

type Props = {};

export default function Content({}: Props) {
  const [allGardenDataList, setAllGardenDataList] = useState<Garden[]>([
    defaultGarden,
  ]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    allGardenService
      .getAllGarden()
      .then((res) => {
        console.log(res.data);
        setAllGardenDataList(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <ContentStyled>
      <WindowLabel />
      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center" }}>
          <CircularProgress />
        </Box>
      ) : (
        <>
          <HeaderRow />
          {allGardenDataList.map((gardenObject, index) => (
            <Row gardenObject={gardenObject} key={index} />
          ))}
        </>
      )}
    </ContentStyled>
  );
}
