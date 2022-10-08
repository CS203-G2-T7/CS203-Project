import React from "react";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";
import { Garden } from "../../Content";

type Props = {
  gardenObject: Garden;
};

/*
    gardenId: "c59f00b6-9bc0-4e45-9b07-2cec834b2e16";
    latitude: "39702.1005";
    location: "Punggol Park";
    longitude: "35327.3650";
    name: "Punggol Garden";
    numPlots: 69;
*/

export default function Row({ gardenObject }: Props) {
  return (
    <RowStyled>
      <RowGardenEntryStyled>
        <span>{gardenObject.name}</span>
        <span>{gardenObject.location}</span>
      </RowGardenEntryStyled>
      <p>{gardenObject.numPlots}</p>
      <p>{gardenObject.numPlots}</p>
      <p>{"Open"}</p>
    </RowStyled>
  );
}
