import React from "react";
import Header from "./Header/Header";
import Content from "./Content/Content";

import { AllGardenStyled } from "./AllGarden.styled";

type Props = {};

export default function Home({}: Props) {
  return (
    <AllGardenStyled>
      <Header />
      <Content />
    </AllGardenStyled>
  );
}
