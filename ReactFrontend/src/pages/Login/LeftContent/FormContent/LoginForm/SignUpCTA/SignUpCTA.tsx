import React from "react";
import { SignUpCTAStyled } from "./SignUpCTA.styled";

type Props = {};

export default function SignUpCTA({}: Props) {
  return (
    <SignUpCTAStyled>
      <span>Don't have an account?</span>
      <span>Sign Up</span>
    </SignUpCTAStyled>
  );
}
