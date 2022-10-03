import React from "react";
import Table from "./Table/Table";
import { ContentStyled } from "./Content.styled";
import WindowLabel from "./WindowLabel/WindowLabel";

type Props = {};

export default function Middle({}: Props) {
  return (
    <ContentStyled>
      <WindowLabel />
      <Table />
    </ContentStyled>
  );
}
