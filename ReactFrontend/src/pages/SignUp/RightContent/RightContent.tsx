import React from "react";
import FirstTextFields from "./FirstTextFields/FirstTextFields";
import { RightContentStyled } from "./RightContent.styled";

type Props = {};

export default function RightContent({}: Props) {
  return (
    <RightContentStyled>
      <h1>Create Account</h1>
      <FirstTextFields/>
    </RightContentStyled>
  );
}
