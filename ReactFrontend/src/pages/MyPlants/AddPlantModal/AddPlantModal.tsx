import React from "react";
import { AddPlantModalStyled } from "./AddPlantModal.styled";
import { SpringValue } from "react-spring";
import { PlantEntryLiStyled } from "./PlantEntryLi.styled";
import { Plant } from "models/Plant";
import plantService from "service/plantService";

type Props = {
  onOverlayClick: React.Dispatch<React.SetStateAction<boolean>>;
  setUserPlantList: React.Dispatch<React.SetStateAction<Plant[]>>;
  style: {
    transform: SpringValue<string>;
  };
  allPlantList: Plant[];
};

export default function AddPlantModal({
  onOverlayClick,
  setUserPlantList,
  style,
  allPlantList,
}: Props) {
  /*categoryClickHandler
    click occurs
    1. Get plant list from parent
    2. Map through list and render
    3. Wait for click
    4. Add plant. Post request.
    5. Close modal.
  */

  const plantLiClickHandler = (
    e: React.MouseEvent<HTMLLIElement, MouseEvent>
  ) => {
    const clickedPlantName: string = e.currentTarget.innerHTML;

    plantService
      .addPlant(clickedPlantName)
      .then(() => {
        setUserPlantList((prevUserPlantList) => {
          const newUserPlantList: Plant[] = prevUserPlantList.slice(0);
          const addedPlant: Plant = allPlantList.filter(
            (plant) =>
              plant.sk ===
              clickedPlantName
          )[0];
          console.log("Added Plant: " + addedPlant);
          newUserPlantList.push(addedPlant);
          return newUserPlantList;
        });
      })
      .catch((err) => {
        console.log(err);
      });
    onOverlayClick(false);
  };

  return (
    <AddPlantModalStyled style={style}>
      <h4>Add Plants</h4>
      <ul>
        {allPlantList.map((plantItem, index) => {
          return (
            <PlantEntryLiStyled
              key={index}
              onClick={(e: React.MouseEvent<HTMLLIElement, MouseEvent>) => {
                plantLiClickHandler(e);
              }}
            >
              {plantItem.sk}
            </PlantEntryLiStyled>
          );
        })}
      </ul>
    </AddPlantModalStyled>
  );
}
