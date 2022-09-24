import React from "react";
import { Link } from "react-router-dom";
import { SignUpCTAStyled } from "./SignUpCTA.styled";

type Props = {};

export default function SignUpCTA({}: Props) {
  return (
    <SignUpCTAStyled>
      <span>Don't have an account?</span>
      <Link to="/sign-up">
        <span>Sign Up</span>
      </Link>
    </SignUpCTAStyled>
  );
}
