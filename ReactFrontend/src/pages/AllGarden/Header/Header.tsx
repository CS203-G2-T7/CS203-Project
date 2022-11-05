import React from "react";
import Navbar from "components/Navbar/Navbar";
import MessageSection from "./MessageSection/MessageSection";
import { HeaderStyled } from "./Header.styled";

type Props = {};
export default function Header({}: Props) {
  return (
    <>
      <Navbar />
      <MessageSection />
    </>
  );
}
