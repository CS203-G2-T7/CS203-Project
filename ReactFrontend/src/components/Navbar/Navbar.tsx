import React from "react";
import LeftSection from "./LeftSection/LeftSection";
import { NavbarStyled } from "./Navbar.styled";
import { RightSectionStyled } from "./RightSection.styled";
import { AccountButton } from "assets/svgs";

type Props = {};

export default function Navbar({}: Props) {
  return (
    <NavbarStyled>
      <LeftSection />
      <RightSectionStyled>
        <a href="/">Home</a>
        <a href="/gardens">Gardens</a>
        <a href="/ballot">Ballot</a>
        <a href="/my-plant">My Plant</a>
        <AccountButton />
      </RightSectionStyled>
    </NavbarStyled>
  );
}
