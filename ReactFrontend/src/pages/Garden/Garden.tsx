import React from "react";
import { useParams } from "react-router-dom";
import { GardenStyled } from "./Garden.styled";

type Props = {};

export default function Garden({}: Props) {
  let params = useParams();
  const gardenName: string =
    params.gardenName?.replace("-", " ") ?? "Garden Not Found";

  return <GardenStyled>{gardenName}</GardenStyled>;
}
