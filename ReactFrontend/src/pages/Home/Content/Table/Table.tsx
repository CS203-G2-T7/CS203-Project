import React from "react";
import { Garden } from "models/Garden";
import HeaderRow from "./HeaderRow/HeaderRow";
import Row from "./Row/Row";
import { Ballot } from "models/Ballot";

type Props = {
  gardenList: Garden[];
  ballotList: Ballot[];
};

export default function Table({ gardenList, ballotList }: Props) {
  //loop through gardenList, use gardenId as key in gardenMap.
  //loop through ballots, get garden.gardenId and increment value in gardenMap.
  const gardenMap = new Map<string, number>();
  gardenList.forEach((garden) => {
    gardenMap.set(garden.gardenId, 0);
  });
  ballotList.forEach((ballot) => {
    const gardenId = ballot.garden.gardenId;
    const prevValue = gardenMap.get(ballot.garden.gardenId);
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
      {gardenList.map((gardenObject, index) => (
        <Row
          gardenObject={gardenObject}
          numBallotsPlaced={gardenMap.get(gardenObject.gardenId) ?? 0}
          key={index}
        />
      ))}
    </>
  );
}
