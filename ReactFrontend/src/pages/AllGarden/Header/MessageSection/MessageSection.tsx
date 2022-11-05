import React from "react";
import {MessageSectionStyled} from "./MessageSection.styled";

type Props = {};

export default function MessageSection({}: Props) {
  return (
    <MessageSectionStyled>
        <h1>Nparks Allotment Garden</h1>
        <h2>Looking for a space to nurture your green fingers?</h2>
    </MessageSectionStyled>
  );
}
