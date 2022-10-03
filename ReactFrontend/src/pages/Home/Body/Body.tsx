import React from "react";
import Middle from "./Middle/Middle"
import { BodyStyled } from "./Body.styled";

type Props = {};

export default function Body({}: Props) {
  return (
    <BodyStyled>
      <Middle />
    </BodyStyled>
  );
}
