import { LandingStyled } from "./Landing.styled";
import React from "react";
import Navbar from "components/Navbar/Navbar";

import {
  FacebookIcon,
  InstagramIcon,
  TwitterIcon,

} from "assets/svgs";



type Props = {};


export default function Login() {
  return (
    <LandingStyled>
      <Navbar></Navbar>
            <FacebookIcon />
            <InstagramIcon />
            <TwitterIcon />
    </LandingStyled>
  );
}


