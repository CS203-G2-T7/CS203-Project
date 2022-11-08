import React from "react";
import { LeftImageStyled } from "./LeftImage.styled";

type Props = {};

export default function LeftContent({}: Props) {
  return (
    <LeftImageStyled
      src="https://www.ourgardenstory.me/image/LeftPottedPlants.png"
      alt="Potted Plants on white background"
    />
  );
}
