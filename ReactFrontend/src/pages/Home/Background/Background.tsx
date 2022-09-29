import React from "react";
import { BackgroundStyled } from "./Background.styled";

type Props = {};

export default function Background({}: Props) {
  return (
    <BackgroundStyled src="./image/DarkGreenSpikyLeaves.jpg" alt="Dark Green Spiky Leaves" />
  );
}
