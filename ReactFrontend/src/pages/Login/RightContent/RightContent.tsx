import { url } from "inspector";
import React from "react";
import { RightImageStyled } from "./RightImage.styled";

type Props = {};

export default function RightContent({}: Props) {
  return (
    <RightImageStyled
      src="https://www.ourgardenstory.me/image/MonsteraImg.jpg"
      alt="Monstera Leaves"
    />
  );
}
