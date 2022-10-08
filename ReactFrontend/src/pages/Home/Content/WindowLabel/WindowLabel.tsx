import React from "react";
import { WindowLabelStyled } from "./WindowLabel.styled";

type Props = {
  windowDate: string;
};

export default function WindowLabel({ windowDate }: Props) {
  //Use this file and the WindowLabel.styled.ts file to create the window label. Refer to figma for the exact design.
  return (
    <WindowLabelStyled>
      <div>
        <h1>Window</h1>
      </div>
      <p>{windowDate}</p>
    </WindowLabelStyled>
  );
}
