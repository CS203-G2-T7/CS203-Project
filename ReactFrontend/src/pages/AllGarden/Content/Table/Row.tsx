import React from "react";
import { Garden } from "models/Garden";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";

type Props = {
  gardenObject: Garden;
};

type LinkStateType = {
  gardenObject: Garden;
};

export default function Row({ gardenObject }: Props) {
  const linkState: LinkStateType = {
    gardenObject: gardenObject,
  };
  return (
    <RowStyled
      state={linkState}
      to={"/garden/" + gardenObject.sk.replace(" ", "-")}
    >
      <RowGardenEntryStyled>
        <span>{gardenObject.sk}</span>
        <span>{gardenObject.gardenAddress}</span>
      </RowGardenEntryStyled>
      <p>{gardenObject.numPlots}</p>
    </RowStyled>
  );
}
