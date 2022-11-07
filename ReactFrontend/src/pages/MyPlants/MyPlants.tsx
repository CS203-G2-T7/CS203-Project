import React, { useEffect, useState } from "react";
import { MyPlantsStyled } from "./MyPlants.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/MyPlants/Header.styled";
import AddPlantModal from "./AddPlantModal/AddPlantModal";
import PlantCard from "./Content/PlantCard";
import { ContentStyled } from "./Content/Content.styled";
import plantService from "service/plantService";
import { Box, CircularProgress, Fab } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { FabStyled } from "./Fab.styled";
import { useTransition } from "react-spring";
import ReactDOM from "react-dom";
import Overlay from "components/Overlay";

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
  const [userPlantList, setUserPlantList] = useState<Plant[]>([]);
  const [allPlantList, setAllPlantList] = useState<Plant[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isError, setIsError] = useState(false);
  const [modalShow, setModalShow] = useState(false);

  const portalElement = document.getElementById("overlays") || new Element();

  useEffect(() => {
    Promise.all([plantService.getAllUserPlants(), plantService.getAllPlants()])
      .then((values) => {
        setUserPlantList(values[0].data);
        setAllPlantList(values[1].data);
        setIsLoading(false);
      })
      .catch((err) => {
        setIsError(true);
        console.log(err);
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
    userPlantList.length === 0 ? (
      <p>You have no plants.</p>
    ) : (
      userPlantList.map((Plant, index) => (
        <PlantCard plant={Plant} key={index} />
      ))
    );
  plantGrid = isError ? <p>Error</p> : plantGrid;

  const menuModalTransition = useTransition(modalShow, {
    from: { transform: "translateY(100vh)" },
    enter: { transform: "translateY(0vh)" },
    leave: { transform: "translateY(100vh)" },
    config: {
      mass: 0.7,
      tension: 238,
      friction: 25,
      velocity: 0.006,
    },
  });
  const overlayTransition = useTransition(modalShow, {
    from: { opacity: 0 },
    enter: { opacity: 1 },
    leave: { opacity: 0 },
    config: {
      mass: 0.7,
      tension: 238,
      friction: 25,
      velocity: 0.006,
    },
  });

  return (
    <>
      {menuModalTransition(
        (styles, menuShow) =>
          menuShow && (
            <>
              {ReactDOM.createPortal(
                <AddPlantModal onOverlayClick={setModalShow} style={styles} allPlantList={allPlantList}/>,
                portalElement
              )}
            </>
          )
      )}
      {overlayTransition(
        (styles, menuShow) =>
          menuShow && (
            <>
              {ReactDOM.createPortal(
                <Overlay onCloseModal={setModalShow} style={styles} />,
                portalElement
              )}
            </>
          )
      )}
      <MyPlantsStyled>
        <NavBarStyled>
          <Navbar />
        </NavBarStyled>
        <HeaderStyled>
          <p>My Plants</p>
          <p>
            View all the plants you are growing in your allotment garden here
          </p>
        </HeaderStyled>
        <ContentStyled>
          {isLoading ? loadingComponent : plantGrid}
        </ContentStyled>
        <FabStyled
          color="primary"
          aria-label="add"
          onClick={() => {
            setModalShow(true);
          }}
        >
          <AddIcon />
        </FabStyled>
      </MyPlantsStyled>
    </>
  );
}
