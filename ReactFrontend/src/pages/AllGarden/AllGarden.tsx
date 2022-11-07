import React from "react";
import Content from "./Content/Content";

import { AllGardenStyled } from "./AllGarden.styled";
import Navbar from "components/Navbar/Navbar";
import { MessageSectionStyled } from "./MessageSection.styled";

type Props = {};

export default function Home({}: Props) {
  return (
    <AllGardenStyled>
      <Navbar />
      <MessageSectionStyled>
        <h1>Nparks Allotment Garden</h1>
        <h2>Looking for a space to nurture your green fingers?</h2>
      </MessageSectionStyled>
      <Content />
    </AllGardenStyled>
  );
}
