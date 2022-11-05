import React from "react";
import { Row as RowType } from "../Content";
import HeaderRow from "./HeaderRow/HeaderRow";
import Row from "./Row/Row";

type Props = {
  rowList: RowType[];
};

export default function Table({ rowList }: Props) {
  return (
    <>
      <HeaderRow />
      {rowList.map((rowItem, index) => (
        <Row
          rowItem={rowItem}
          key={index}
        />
      ))}
    </>
  );
}
