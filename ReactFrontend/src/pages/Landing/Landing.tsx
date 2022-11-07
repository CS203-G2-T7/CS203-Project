import { LandingStyled } from "./Landing.styled";
import { NavBarStyled } from "./NavBar.styled";
import { HeaderStyled } from "./Header.styled";
import { FooterStyled } from "./Footer.styled";
import { BannerStyled } from "./Banner.styled";
import SocialMedia from "./SocialMedia/SocialMedia";

import React, { useEffect } from "react";
import Navbar from "components/Navbar/Navbar";
import Content from "./Content/Content";
import { useNavigate } from "react-router-dom";
import landingService from "service/landingService";

export default function Landing() {
  const navigate = useNavigate();
  const loadHandler = (): void => {
    landingService
      .checkLoggedIn()
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
        navigate("/login");
      });
  };

  useEffect(() => {
    loadHandler();
  }, []);

  return (
    <LandingStyled>
      <NavBarStyled>
        <Navbar />
      </NavBarStyled>
      <HeaderStyled>
        <h1>
          Explore <br></br> the Garden <br></br> with us today
        </h1>
      </HeaderStyled>
      <BannerStyled>
        <p>Our Allotment Gardens</p>
      </BannerStyled>
      <Content />
      <SocialMedia />

      <FooterStyled></FooterStyled>
    </LandingStyled>
  );
}
