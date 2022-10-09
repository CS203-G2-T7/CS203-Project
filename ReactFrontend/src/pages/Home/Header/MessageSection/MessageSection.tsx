import React from "react";
import {MessageSectionStyled} from "./MessageSection.styled";

type Props = {};

export default function MessageSection({}: Props) {
  return (
    <MessageSectionStyled>
        <h1>Be one with nature</h1>
        <h2>Building communities around gardens</h2>
    </MessageSectionStyled>
  );
}
