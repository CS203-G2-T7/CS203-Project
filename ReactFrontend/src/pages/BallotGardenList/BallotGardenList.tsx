import React from "react";
import Content from "./Content/Content";

import { HomeStyled } from "./Home.styled";
import Navbar from "components/Navbar/Navbar";
import { MessageSectionStyled } from "./MessageSection.styled";

type Props = {};

export default function BallotGardenList({}: Props) {
  return (
    <HomeStyled>
      <Navbar />
      <MessageSectionStyled>
        <h1>Be one with nature</h1>
        <h2>Building communities around gardens</h2>
      </MessageSectionStyled>
      <Content />
    </HomeStyled>
  );
}
