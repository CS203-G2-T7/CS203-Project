import React from "react";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import Box from "@mui/material/Box";

import { HorizontalContainerStyled } from "./HorizontalContainer.styled";
import { SectionBoxStyled } from "components/SectionBox.styled";
import Carousel from "./Carousel/Carousel";
import Details from "./Details/Details";
import { LinkStateType } from "../Garden";

type Props = {
  linkState: LinkStateType;
};

export default function Summary({linkState}: Props) {
  return (
    <SectionBoxStyled>
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs value={0}>
          <Tab label="Summary" />
        </Tabs>
      </Box>
      <HorizontalContainerStyled>
        <Carousel />
        <Details linkState={linkState}/>
      </HorizontalContainerStyled>
    </SectionBoxStyled>
  );
}
