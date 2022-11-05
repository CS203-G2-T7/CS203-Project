import React from "react";
import { Garden } from "models/Garden";
import HeaderRow from "./HeaderRow/HeaderRow";
import Row from "./Row/Row";

type Props = {
  gardenList: Garden[];
};

export default function Table({ gardenList }: Props) {
  const gardenMap = new Map<string, number>();
  gardenList.forEach((garden) => {
    console.log(garden);
    gardenMap.set(garden.sk, 0);
  });

  return (
    <>
      <HeaderRow />
      {gardenList.map((gardenObject, index) => (
        <Row gardenObject={gardenObject} key={index} />
      ))}
    </>
  );
}
