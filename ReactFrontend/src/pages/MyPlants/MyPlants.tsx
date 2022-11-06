import React, { useEffect, useState } from "react";
import { MyPlantsStyled } from "./MyPlants.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/MyPlants/Header.styled";
import PlantCard from "./Content/PlantCard";
import { ContentStyled } from "./Content/Content.styled";
import plantService from "service/plantService";

export type Plant = {
  plantSpecies: string;
  description: string;
  sk: string;
  pk: string;
};

export const defaultPlant: Plant = {
  plantSpecies: "",
  description: "",
  sk: "",
  pk: "",
};

type Props = {};
export default function MyPlants({}: Props) {
  const [plantDataList, setPlantDataList] = useState<Plant[]>([defaultPlant]);

  useEffect(() => {
    Promise.all([plantService.getAllUserPlants()])
      .then((resArr) => {
        console.log(resArr[0].data);
        setPlantDataList(resArr[0].data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

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
        {plantDataList.map((Plant, index) => (
          <PlantCard plant={Plant} key={index}/>
        ))}
      </ContentStyled>
    </MyPlantsStyled>
  );
}
