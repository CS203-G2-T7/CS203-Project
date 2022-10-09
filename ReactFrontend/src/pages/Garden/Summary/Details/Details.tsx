import { Button } from "@mui/material";
import React from "react";
import { DetailsRowStyled } from "./DetailRow.styled";
import { DetailsGroupStyled } from "./DetailsGroup.styled";
import { DetailsRightStyled } from "./DetailsRight.styled";
import { SummaryButtonStyled } from "./SummaryButton.styled";

type Props = {};

export default function Details({}: Props) {
  return (
    <DetailsRightStyled>
      <DetailsGroupStyled>
        <DetailsRowStyled>
          <h3>Garden Details</h3>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Number of plots</p>
          <p>80</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Available plots</p>
          <p>18</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Lease date</p>
          <p>01/05/2022 - 01/05/2025</p>
        </DetailsRowStyled>
      </DetailsGroupStyled>
      <DetailsGroupStyled>
        <DetailsRowStyled>
          <h3>Ballot Details</h3>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Window</p>
          <p>01/04/2022 - 01/05/2022</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Ballots placed</p>
          <p>32</p>
        </DetailsRowStyled>
      </DetailsGroupStyled>
      <SummaryButtonStyled variant="contained">Place Bid</SummaryButtonStyled>
    </DetailsRightStyled>
  );
}
