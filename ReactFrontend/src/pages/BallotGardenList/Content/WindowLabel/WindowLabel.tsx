import { Window } from "models/Window";
import React from "react";
import { WindowLabelStyled } from "./WindowLabel.styled";

type Props = {
  windowData: Window;
};

export default function WindowLabel({ windowData }: Props) {
  const startDate = windowData.sk;

  const endDate = windowData.windowDuration;

  return (
    <WindowLabelStyled>
      <div>
        <h1>Window</h1>
      </div>
      <p>{startDate + " " + endDate || "loading..."}</p>
    </WindowLabelStyled>
  );
}
