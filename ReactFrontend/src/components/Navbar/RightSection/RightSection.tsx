import React from "react";
import { RightSectionStyled } from "./RightSection.styled";
import { AccountButton } from "assets/svgs";

type Props = {};

export default function RightSection({}: Props) {
  return (
    <RightSectionStyled>
      <a href="/myplants">MyPlants</a>
      <a href="/Home">Ballot</a>
      <a href="/allgarden">Gardens</a>
      <AccountButton />
    </RightSectionStyled>
  );
}
