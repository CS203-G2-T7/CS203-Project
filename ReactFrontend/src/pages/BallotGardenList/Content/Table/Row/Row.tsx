import React from "react";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";
import { Row as RowType } from "../../Content";

type Props = {
  rowItem: RowType;
};

export default function Row({ rowItem }: Props) {
  const urlName: string = rowItem.gardenName.replace(" ", "-");
  return (
    <RowStyled to={`/ballot/${urlName}`}>
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
