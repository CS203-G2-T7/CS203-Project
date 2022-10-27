import { LandingStyled } from "./Landing.styled";
import { NavBarStyled } from "./NavBar.styled";
import React from "react";
import Navbar from "components/Navbar/Navbar";
import Content from "./Content/Content";


import {
  FacebookIcon,
  InstagramIcon,
  TwitterIcon,

} from "assets/svgs";



type Props = {};


export default function Landing() {
  return (
    <LandingStyled>

      <NavBarStyled>
        <Navbar />
      </NavBarStyled>
      <Content />
            <FacebookIcon />
            <InstagramIcon />
            <TwitterIcon />
    </LandingStyled>
  );
}


