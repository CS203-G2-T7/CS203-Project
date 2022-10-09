import React from "react";
import { Garden } from "models/Garden";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";
import { number } from "yargs";
import { Window } from "models/Window";

type Props = {
  gardenObject: Garden;
  numBallotsPlaced: number;
  windowData: Window;
};

type LinkStateType = {
  gardenObject: Garden;
  numBallotsPlaced: number;
  windowData: Window;
};

export default function Row({
  gardenObject,
  numBallotsPlaced,
  windowData,
}: Props) {
  const linkState: LinkStateType = {
    gardenObject: gardenObject,
    numBallotsPlaced: numBallotsPlaced,
    windowData: windowData,
  };
  return (
    <RowStyled state={linkState} to={"/garden?id=" + gardenObject.gardenId}>
      <RowGardenEntryStyled>
        <span>{gardenObject.name}</span>
        <span>{gardenObject.location}</span>
      </RowGardenEntryStyled>
      <p>{gardenObject.numPlots}</p>
      <p>{numBallotsPlaced}</p>
      <p>{"Open"}</p>
    </RowStyled>
  );
}
