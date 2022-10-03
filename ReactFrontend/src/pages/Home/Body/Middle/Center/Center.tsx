import React from "react";
import HeaderRow from "./HeaderRow/HeaderRow";
import Row1 from "./Row1/Row1";
import { CenterStyled } from "./Center.styled";

type Props = {};

export default function Center({}: Props) {
  return (
    <CenterStyled>
        <HeaderRow />
        <Row1 />
    </CenterStyled>
  );
}
