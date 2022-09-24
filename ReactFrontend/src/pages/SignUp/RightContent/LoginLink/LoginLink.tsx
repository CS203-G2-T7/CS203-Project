import React from "react";
import { Link } from "react-router-dom";
import { LoginLinkStyled } from "./LoginLink.styled";

type Props = {};

export default function LoginLink({}: Props) {
  return (
    <LoginLinkStyled>
      <p>Already have an account?</p>
      <Link to="/login">
        <p>Sign In</p>
      </Link>
    </LoginLinkStyled>
  );
}
