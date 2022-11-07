import { Window } from "models/Window";
import React from "react";
import { WindowLabelStyled } from "./WindowLabel.styled";
import dayjs from "dayjs";
import duration from "dayjs/plugin/duration";

type Props = {
  windowData: Window;
};

export default function WindowLabel({ windowData }: Props) {
  dayjs.extend(duration);

  const startDate = dayjs(windowData.sk, "MM-DD-YYYY");
  const windowPeriod = dayjs.duration(windowData.windowDuration);

  const endDate = startDate.add(windowPeriod);

  return (
    <WindowLabelStyled>
      <div>
        <h1>Window</h1>
      </div>
      <p>
        {startDate.format("DD/MM/YYYY") +
          " - " +
          endDate.format("DD/MM/YYYY") || "loading..."}
      </p>
    </WindowLabelStyled>
  );
}
