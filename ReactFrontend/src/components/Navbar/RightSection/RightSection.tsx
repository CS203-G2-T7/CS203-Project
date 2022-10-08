import { randomFillSync } from "crypto";
import React from "react";
import { TransformStream } from "stream/web";
import { RightSectionStyled } from "./RightSection.styled";
import { AccountButton } from "assets/svgs";

type Props = {};

export default function RightSection({}: Props) {
  return (
    <RightSectionStyled>
      <a href="/">Home</a>
      <a href="/">About</a>
      <a href="/">Gardens</a>
      <AccountButton />
    </RightSectionStyled>
  );
}
