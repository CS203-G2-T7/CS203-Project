import React from "react";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import Box from "@mui/material/Box";
import { SummaryStyled } from "./Summary.styled";

type Props = {};

export default function Summary({}: Props) {
  return (
    <SummaryStyled>
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs value={0}>
          <Tab label="Summary" />
        </Tabs>
      </Box>

      {/* carousel left side */}
      {/* details right side */}
    </SummaryStyled>
  );
}
