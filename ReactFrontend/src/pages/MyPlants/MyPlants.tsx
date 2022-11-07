import React, { useEffect, useState } from "react";
import { MyPlantsStyled } from "./MyPlants.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/MyPlants/Header.styled";
import PlantCard from "./Content/PlantCard";
import { ContentStyled } from "./Content/Content.styled";
import plantService from "service/plantService";
import { Box, CircularProgress } from "@mui/material";

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

export default function MyPlants() {
  const [plantDataList, setPlantDataList] = useState<Plant[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    plantService
      .getAllUserPlants()
      .then((res) => {
        console.log(res.data);
        setPlantDataList(res.data);
        setIsLoading(false);
      })
      .catch((err) => {
        console.log(err);
        setIsLoading(false);
        setIsError(true);
      });
  }, []);

  const loadingComponent: React.ReactNode = (
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        margin: "auto",
      }}
    >
      <CircularProgress />
    </Box>
  );

  let plantGrid: React.ReactNode =
    plantDataList.length === 0 ? (
      <p>You have no plants.</p>
    ) : (
      plantDataList.map((Plant, index) => (
        <PlantCard plant={Plant} key={index} />
      ))
    );
  plantGrid = isError ? <p>Error</p> : plantGrid;

  return (
    <MyPlantsStyled>
      <NavBarStyled>
        <Navbar />
      </NavBarStyled>
      <HeaderStyled>
        <p>My Plants</p>
        <p>View all the plants you are growing in your allotment garden here</p>
      </HeaderStyled>
      <ContentStyled>{isLoading ? loadingComponent : plantGrid}</ContentStyled>
    </MyPlantsStyled>
  );
}
