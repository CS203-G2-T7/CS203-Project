import React from "react";
import { Garden } from "models/Garden";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";
import { Window } from "models/Window";
import { Row as RowType } from "../../Content";

type Props = {
  rowItem: RowType;
};

export default function Row({ rowItem }: Props) {
  return (
    <RowStyled to={"/garden?id="}>
      <RowGardenEntryStyled>
        <span>{rowItem.gardenName}</span>
        <span>{rowItem.gardenAddress}</span>
      </RowGardenEntryStyled>
      <p>{rowItem.availablePlots}</p>
      <p>{rowItem.ballotsPlaced}</p>
      <p>{rowItem.status}</p>
    </RowStyled>
  );
}
