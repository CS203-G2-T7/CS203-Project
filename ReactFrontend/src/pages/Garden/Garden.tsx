import { Garden as GardenType } from "models/Garden";
import React from "react";
import { useLocation } from "react-router";
import Details from "./Details/Details";
import { GardenStyled } from "./Garden.styled";
import Header from "./Header/Header";
import Summary from "./Summary/Summary";
import { Window } from "models/Window";

type Props = {};

export type LinkStateType = {
  gardenObject: GardenType;
  numBallotsPlaced: number;
  windowData: Window;
};

export default function Garden({}: Props) {
  const location = useLocation();
  const linkState = location.state as LinkStateType;
  console.log(linkState);
  // console.log(linkState.numBallotsPlaced);

  return (
    <GardenStyled>
      <Header
        name={linkState.gardenObject.name}
        address={linkState.gardenObject.location}
      />
      <main>
        <Summary linkState={linkState}/>
        <Details />
      </main>
    </GardenStyled>
  );
}