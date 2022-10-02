import React from "react";
import Header from "./Header/Header";
import Body from "./Body/Body";

import { HomeStyled } from "./Home.styled";

type Props = {};

export default function Home({}: Props) {
  return (
    <>
      <HomeStyled>
        <Header />
        <Body />
      </HomeStyled>
    </>
  );
}
