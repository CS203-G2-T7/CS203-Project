import React, { useEffect, useState } from "react";
import { defaultGarden, Garden } from "models/Garden";
import { RowStyled } from "./Row.styled";
import { RowGardenEntryStyled } from "./RowGardenEntry.styled";
import { number } from "yargs";
import { Window } from "models/Window";
import { GardenWin } from "models/GardenWin";
import homeService from "service/homeService";

type Props = {
  gardenWinObject: GardenWin;
  numBallotsPlaced: number;
  windowData: Window;
};

type LinkStateType = {
  gardenObject: Garden;
  numBallotsPlaced: number;
  windowData: Window;
};

export default function Row({
  gardenWinObject,
  numBallotsPlaced,
  windowData,
}: Props) {
  const [gardenData, setGardenData] = useState<Garden>(defaultGarden);
  useEffect(() => {
    Promise.all([
      homeService.getGardenByName(gardenWinObject.sk),
    ])
      .then((resArr) => {
        console.log("idis")
        console.log(resArr[0].data);
        setGardenData(resArr[0].data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  const linkState: LinkStateType = {
    gardenObject: gardenData,
    numBallotsPlaced: numBallotsPlaced,
    windowData: windowData,
  };
  return (
    <RowStyled state={linkState} to={"/garden?id=" + gardenData.sk}>
      <RowGardenEntryStyled>
        <span>{gardenData.sk}</span>
        <span>{gardenData.gardenAddress}</span>
      </RowGardenEntryStyled>
      <p>{gardenData.numPlots}</p>
      <p>{numBallotsPlaced}</p>
      <p>{"Open"}</p>
    </RowStyled>
  );
}
