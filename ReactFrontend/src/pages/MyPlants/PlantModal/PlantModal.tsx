import React from "react";
import { SpringValue } from "react-spring";
import { Plant } from "models/Plant";
import { PlantModalStyled } from "./PlantModal.styled";

type Props = {
  style: {
    transform: SpringValue<string>;
  };
  plant: Plant;
};

export default function PlantModal({ style, plant }: Props) {
  return (
    <PlantModalStyled style={style}>
      <div>
        <img src={require(`assets/imgs/${plant.sk}.jpg`)} alt={plant.sk} />
      </div>
      <div>
        <h1>{plant.sk}</h1>
        <h2>{plant.plantSpecies}</h2>
        <p>{plant.description}</p>
      </div>
    </PlantModalStyled>
  );
}
