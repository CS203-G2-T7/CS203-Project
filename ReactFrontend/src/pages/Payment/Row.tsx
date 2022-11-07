import React from "react";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";

type Props = {
  gardenName: string;
  windowId: string;
  paymentAmount: number;
};

export default function Row({ gardenName, windowId, paymentAmount }: Props) {
  return (
    <RowStyled>
      <RowGardenEntryStyled>
        <span>{gardenName}</span>
        <span>{windowId}</span>
      </RowGardenEntryStyled>
      <p>{"$" + paymentAmount}</p>
    </RowStyled>
  );
}
