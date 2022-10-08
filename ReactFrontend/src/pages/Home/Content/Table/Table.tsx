import React from "react";
import { Garden } from "../Content";
import HeaderRow from "./HeaderRow/HeaderRow";
import Row from "./Row/Row";
import { TableStyled } from "./Table.styled";

export type rowObject = {
  name: string;
  address: string;
  availablePlots: number;
  ballotsPlaced: number;
  status: string;
};

const fakeDatabase: rowObject[] = [
  {
    name: "Ang Mo Kio Garden",
    address: "609 Ang Mo Kio Ave 1, Singapore 569973",
    availablePlots: 3,
    ballotsPlaced: 20,
    status: "Open",
  },
  {
    name: "Bishan Allotment Garden",
    address: "609 Bishan Ave 1, Singapore 569973",
    availablePlots: 3,
    ballotsPlaced: 20,
    status: "Closed",
  },
  {
    name: "Toa Payoh Allotment Garden",
    address: "609 Toa Payoh Ave 1, Singapore 569973",
    availablePlots: 3,
    ballotsPlaced: 20,
    status: "Open",
  },
  {
    name: "Some very very long name so it will overflow Allotment Garden",
    address:
      "609 Ang Mo Kio Ave 1 Some very long address so it will overflow, Singapore 569973",
    availablePlots: 3,
    ballotsPlaced: 20,
    status: "Closed",
  },
];


type Props = {
  gardenList: Garden[];
};

export default function Table({ gardenList }: Props) {
  return (
    <>
      <HeaderRow />
      {gardenList.map((gardenObject, index) => (
        <Row gardenObject={gardenObject} key={index} />
      ))}
    </>
  );
}
