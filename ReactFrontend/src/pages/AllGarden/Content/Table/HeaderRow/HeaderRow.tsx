import React from "react";
import { HeaderRowStyled } from "./HeaderRow.styled";

type Props = {};

export default function HeaderRow({}: Props) {
  return (
    <HeaderRowStyled>
      <div>Garden</div>
      <div>Number of plots</div>
    </HeaderRowStyled>
  );
}
