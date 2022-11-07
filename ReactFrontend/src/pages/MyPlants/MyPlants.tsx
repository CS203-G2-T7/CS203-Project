import React, { useEffect, useState } from "react";
import { MyPlantsStyled } from "./MyPlants.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/MyPlants/Header.styled";
import AddPlantModal from "./AddPlantModal/AddPlantModal";
import PlantCard from "./Content/PlantCard";
import { ContentStyled } from "./Content/Content.styled";
import plantService from "service/plantService";
import { Box, CircularProgress } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { FabStyled } from "./Fab.styled";
import { useTransition } from "react-spring";
import ReactDOM from "react-dom";
import Overlay from "components/Overlay";
import PlantModal from "./PlantModal/PlantModal";

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
  const [addPlantModal, setAddPlantModal] = useState(false);
  const [plantModal, setPlantModal] = useState(false);
  const [clickedPlant, setClickedPlant] = useState<Plant>(defaultPlant);

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
        <PlantCard
          plant={Plant}
          key={index}
          setPlantModal={setPlantModal}
          setClickedPlant={setClickedPlant}
        />
      ))
    );
  plantGrid = isError ? <p>Error</p> : plantGrid;

  const addPlantModalTransition = useTransition(addPlantModal, {
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
  const addPlantOverlayTransition = useTransition(addPlantModal, {
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

  const plantModalTransition = useTransition(plantModal, {
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
  const plantOverlayTransition = useTransition(plantModal, {
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
      {addPlantModalTransition(
        (styles, menuShow) =>
          menuShow && (
            <>
              {ReactDOM.createPortal(
                <AddPlantModal
                  onOverlayClick={setAddPlantModal}
                  style={styles}
                  allPlantList={allPlantList}
                  setUserPlantList={setUserPlantList}
                />,
                portalElement
              )}
            </>
          )
      )}
      {addPlantOverlayTransition(
        (styles, menuShow) =>
          menuShow && (
            <>
              {ReactDOM.createPortal(
                <Overlay onCloseModal={setAddPlantModal} style={styles} />,
                portalElement
              )}
            </>
          )
      )}

      {plantModalTransition(
        (styles, menuShow) =>
          menuShow && (
            <>
              {ReactDOM.createPortal(
                <PlantModal style={styles} plant={clickedPlant} />,
                portalElement
              )}
            </>
          )
      )}
      {plantOverlayTransition(
        (styles, menuShow) =>
          menuShow && (
            <>
              {ReactDOM.createPortal(
                <Overlay onCloseModal={setPlantModal} style={styles} />,
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
            setAddPlantModal(true);
          }}
        >
          <AddIcon />
        </FabStyled>
      </MyPlantsStyled>
    </>
  );
}
