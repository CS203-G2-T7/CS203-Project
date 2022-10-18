import { randomFillSync } from "crypto";
import React from "react";
import { TransformStream } from "stream/web";
import { RightSectionStyled } from "./RightSection.styled";
import { AccountButton } from "assets/svgs";
import ballotService from "service/ballotService";

type Props = {};

export default function RightSection({}: Props) {
  const chooseBallotHandler = () => {
    console.log("Choose ballot clicked.");
    ballotService
      .chooseBallot()
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };
  return (
    <RightSectionStyled>
      <a href="/">Home</a>
      <a href="/">About</a>
      <a href="/">Gardens</a>
      <AccountButton onClick={() => chooseBallotHandler()} />
    </RightSectionStyled>
  );
}