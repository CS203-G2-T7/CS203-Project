import React from "react";
import { RightSectionStyled } from "./RightSection.styled";
import { AccountButton } from "assets/svgs";

type Props = {};

export default function RightSection({}: Props) {
  return (
    <RightSectionStyled>
      <a href="/landing">Home</a>
      <a href="/my-plant">MyPlants</a>
      <a href="/ballot">Ballot</a>
      <a href="/gardens">Gardens</a>
      <AccountButton />
    </RightSectionStyled>
  );
}
