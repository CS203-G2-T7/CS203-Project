import React from "react";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";
import { rowObject } from "../Table";

type Props = {
  rowObject: rowObject;
};

export default function Row({ rowObject }: Props) {
  return (
    <RowStyled>
      <RowGardenEntryStyled>
        <span>{rowObject.name}</span>
        <span>{rowObject.address}</span>
      </RowGardenEntryStyled>
      <p>{rowObject.availablePlots}</p>
      <p>{rowObject.ballotsPlaced}</p>
      <p>{rowObject.status}</p>
    </RowStyled>
  );
}
