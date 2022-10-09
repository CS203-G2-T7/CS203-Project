import React from "react";
import Header from "./Header/Header";
import Content from "./Content/Content";

import { HomeStyled } from "./Home.styled";

type Props = {};

export default function Home({}: Props) {
  return (
    <HomeStyled>
      <Header />
      <Content />
    </HomeStyled>
  );
}
