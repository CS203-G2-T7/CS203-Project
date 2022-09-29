import React from "react";
import Background from "./Background/Background";
import Top from "./Top/Top";
import Bottom from "./Bottom/Bottom";

import { HomeStyled } from "./Home.styled";

type Props = {};

export default function Home({}: Props) {
  return (
    <>
      <HomeStyled>
        <Top />
        <Bottom />
        {/* <Background /> */}
      </HomeStyled>
    </>
  );
}
