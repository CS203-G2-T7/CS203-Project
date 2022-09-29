import React from "react";
import LeftSection from "./LeftSection/LeftSection";
import RightSection from "./RightSection/RightSection";
import { HeaderStyled } from "./Header.styled";

type Props = {};

//TODO: Rename to Navbar
export default function Header({}: Props) {
  return (
    <HeaderStyled>
      <LeftSection />
      <RightSection />
    </HeaderStyled>
  );
}
