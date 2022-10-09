import Navbar from "components/Navbar/Navbar";
import React from "react";
import { HeaderStyled } from "./Header.styled";

type Props = {
  name: string;
  address: string;
};

export default function Header({ name, address }: Props) {
  return (
    <HeaderStyled>
      <Navbar />
      <h1>{name}</h1>
      <h2>{address}</h2>
    </HeaderStyled>
  );
}
