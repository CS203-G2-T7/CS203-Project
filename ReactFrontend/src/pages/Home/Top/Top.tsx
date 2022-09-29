import React from "react";
import Header from "./Header/Header"
import MessageSection from './MessageSection/MessageSection';
import { TopStyled } from "./Top.styled";


type Props = {};

export default function Top({}: Props) {
  return (
    <TopStyled>
      <Header />
      <MessageSection />
    </TopStyled>
  );
}
