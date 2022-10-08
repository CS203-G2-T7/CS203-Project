import { WindowOptionArrow } from "assets/svgs";
import React from "react";
import { WindowLabelStyled } from "./WindowLabel.styled";

type Props = {};

export default function WindowLabel({}: Props) {
  //Use this file and the WindowLabel.styled.ts file to create the window label. Refer to figma for the exact design.
  return <WindowLabelStyled>
    <div>
      <h1>Window 3</h1>
      <WindowOptionArrow />
    </div>
    <p>1/10/2022 - 31/10/2022</p>
  </WindowLabelStyled>;
}
