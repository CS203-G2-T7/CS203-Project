import React from "react";
import { Garden } from "models/Garden";
import HeaderRow from "./HeaderRow/HeaderRow";
import Row from "./Row/Row";
import { Ballot } from "models/Ballot";
import { Window } from "models/Window";
import { GardenWin } from "models/GardenWin";

type Props = {
  gardenWinList: GardenWin[];
  ballotList: Ballot[];
  windowData: Window;
};

export default function Table({ gardenWinList, ballotList, windowData }: Props) {
  //loop through gardenList, use gardenId as key in gardenMap.
  //loop through ballots, get garden.gardenId and increment value in gardenMap.
  const gardenMap = new Map<string, number>();
  gardenWinList.forEach((gardenWin) => {
    gardenMap.set(gardenWin.sk, 0);
  });
  ballotList.forEach((ballot) => {
    const gardenId = ballot.garden.gardenId;
    const prevValue = gardenMap.get(gardenId);
    if (
      gardenId !== "" &&
      gardenId !== null &&
      gardenId !== undefined &&
      prevValue !== undefined
    ) {
      gardenMap.set(gardenId, prevValue + 1);
    }
  });
  // console.log(gardenMap);

  return (
    <>
      <HeaderRow />
      {gardenWinList.map((gardenObject, index) => (
        <Row
          gardenWinObject={gardenObject}
          windowData={windowData}
          numBallotsPlaced={gardenMap.get(gardenObject.gardenId) ?? 0}
          key={index}
        />
      ))}
    </>
  );
}
