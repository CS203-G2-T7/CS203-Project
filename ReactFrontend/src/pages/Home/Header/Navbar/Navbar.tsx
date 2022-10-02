import React from "react";
import LeftSection from "./LeftSection/LeftSection";
import RightSection from "./RightSection/RightSection";
import { NavbarStyled } from "./Navbar.styled";

type Props = {};

export default function Navbar({}: Props) {
  return (
    <NavbarStyled>
      <LeftSection />
      <RightSection />
    </NavbarStyled>
  );
}
