import React from "react";
import { HeaderRowStyled } from "./HeaderRow.styled";

type Props = {};

export default function HeaderRow({}: Props) {
  return (
    <HeaderRowStyled>
      <span>Garden</span>
      <span>Available plots</span>
      <span>Ballots placed</span>
      <span>Status</span>
    </HeaderRowStyled>
  );
}
