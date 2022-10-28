import { LandingStyled } from "./Landing.styled";
import { NavBarStyled } from "./NavBar.styled";
import React from "react";
import Navbar from "components/Navbar/Navbar";
import Content from "./Content/Content";

import { FacebookIcon, InstagramIcon, TwitterIcon } from "assets/svgs";

type Props = {};

export default function Landing() {
  return (
    <LandingStyled>
      <NavBarStyled>
        <Navbar />
      </NavBarStyled>
      <h1>Explore </h1>
      <h1>the Garden </h1>
      <h1>With Us Today</h1>
      <Content />
      <FacebookIcon />
      <InstagramIcon />
      <TwitterIcon />
    </LandingStyled>
  );
}
