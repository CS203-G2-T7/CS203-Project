import React from "react";
import { Garden } from "models/Garden";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";

type Props = {
  gardenObject: Garden;
  numBallotsPlaced: number;
};

export default function Row({ gardenObject, numBallotsPlaced }: Props) {
  return (
    <RowStyled to={"/garden?id=" + gardenObject.gardenId}>
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
