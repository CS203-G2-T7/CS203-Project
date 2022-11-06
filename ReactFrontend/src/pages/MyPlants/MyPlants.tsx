import React from "react";
import { MyPlantsStyled } from "./MyPlants.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/MyPlants/Header.styled";
import PlantCard from "./Content/PlantCard";
import { ContentStyled } from "./Content/Content.styled";

export type rowObject = {
  plantName: string;
  species: string;
};

const fakeDatabase: rowObject[] = [
  {
    plantName: "Lettuce",
    species: "Lactuca sativa",
  },
  {
    plantName: "Sweet Potato",
    species: "Ipomoea batatas",
  },
  {
    plantName: "Brinjal",
    species: "Solanum melongena",
  },

  {
    plantName: "Brinjal",
    species: "Solanum melongena",
  },

  {
    plantName: "Brinjal",
    species: "Solanum melongena",
  },
];

type Props = {};

export default function MyPlants({}: Props) {
  return (
    <MyPlantsStyled>
      <NavBarStyled>
        <Navbar />
      </NavBarStyled>
      <HeaderStyled>
        <p>My Plants</p>
        <p>View all the plants you are growing in your allotment garden here</p>
      </HeaderStyled>
      <ContentStyled>
        {fakeDatabase.map((rowObject) => (
          <PlantCard rowObject={rowObject} />
        ))}
      </ContentStyled>
    </MyPlantsStyled>
  );
}
