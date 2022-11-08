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
        <a href="/garden">Gardens</a>
        <a href="/ballot">Ballot</a>
        <a href="/my-plant">MyPlant</a>
        <a href="/community">MyCommunity</a>
        <a href="/payment">Payment</a>
        <a href="/login">Logout</a>

        <AccountButton />
      </RightSectionStyled>
    </NavbarStyled>
  );
}
