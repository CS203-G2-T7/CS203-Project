import React from "react";
import { MyPlantsStyled } from "./MyPlants.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/MyPlants/Header.styled";

export default function Login() {
  return (
    <MyPlantsStyled>
      <NavBarStyled>
        <Navbar />
      </NavBarStyled>
      <HeaderStyled>
        <p>My Plants</p>
        <p>View all the plants you are growing in your allotment garden here</p>
      </HeaderStyled>
    </MyPlantsStyled>
  );
}
