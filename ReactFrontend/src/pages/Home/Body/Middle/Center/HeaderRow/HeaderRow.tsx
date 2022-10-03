import React from "react";
import Garden from "./Garden/Garden";
import Plots from "./Plots/Plots"
import BallotsPlaced from "./BallotsPlaced/BallotsPlaced";
import Status from "./Status/Status";
import { HeaderRowStyled } from "./HeaderRow.styled";

type Props = {};

export default function HeaderRow({}: Props) {
  return (
    <HeaderRowStyled>
      <Garden />
      <Plots />
      <BallotsPlaced />
      <Status />
    </HeaderRowStyled>
  );
}
