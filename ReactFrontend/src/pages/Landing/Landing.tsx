import { LandingStyled } from "./Landing.styled";
import { NavBarStyled } from "./NavBar.styled";
import { HeaderStyled } from "./Header.styled";
import { FooterStyled } from "./Footer.styled";


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
      <HeaderStyled>
        <h1>Explore <br></br> the Garden <br></br> with us today</h1>
      </HeaderStyled>
      <Content />
      <FacebookIcon />
      <InstagramIcon />
      <TwitterIcon />
      <FooterStyled></FooterStyled>
    </LandingStyled>
  );
}
