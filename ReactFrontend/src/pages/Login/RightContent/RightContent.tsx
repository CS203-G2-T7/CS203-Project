import React from "react";
import { RightContentStyled } from "./RightContent.styled";
import { RightImageStyled } from "./RightImage.styled";

type Props = {};

export default function RightContent({}: Props) {
  return (
    <RightImageStyled src="./image/MonsteraImg.jpg" alt="Monstera Leaves" />
    // <RightContentStyled>
    // </RightContentStyled>
  );
}
