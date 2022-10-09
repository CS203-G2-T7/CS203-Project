import Navbar from "components/Navbar/Navbar";
import React from "react";
import { HeaderStyled } from "./Header.styled";

type Props = {};

export default function Header({}: Props) {
  return (
    <HeaderStyled>
      <Navbar />
      <h1>{"Ang Mo Kio Allotment Garden"}</h1>
      <h2>{"609 Ang Mo Kio Ave 1, Singapore 569973"}</h2>
    </HeaderStyled>
  );
}
