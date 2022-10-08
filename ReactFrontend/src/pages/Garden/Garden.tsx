import React from "react";
import Details from "./Details/Details";
import { GardenStyled } from "./Garden.styled";
import Header from "./Header/Header";
import Summary from "./Summary/Summary";

type Props = {};

export default function Garden({}: Props) {
  return (
    <GardenStyled>
      <Header />
      <main>
        <Summary />
        <Details />
      </main>
    </GardenStyled>
  );
}
