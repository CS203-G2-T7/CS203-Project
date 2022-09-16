import React from "react";
import { RightContentStyled } from "./RightContent.styled";
import { RightImageStyled } from "./RightImage.styled";

type Props = {};

export default function RightContent({}: Props) {
  return (
    <RightContentStyled>
      <RightImageStyled
        src="./image/Right content leaf image.png"
        alt="Leaves"
      />
    </RightContentStyled>
  );
}
