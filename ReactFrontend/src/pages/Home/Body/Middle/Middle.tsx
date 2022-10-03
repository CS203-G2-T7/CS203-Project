import React from "react";
import Center from "./Center/Center"
import { MiddleStyled } from "./Middle.styled";

type Props = {};

export default function Middle({}: Props) {
  return (
    <MiddleStyled>
      <Center />
    </MiddleStyled>
  );
}
