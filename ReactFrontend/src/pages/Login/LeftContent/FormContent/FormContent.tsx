import React from "react";
import { FormContentStyle } from "./FormContent.style";
import LoginForm from "./LoginForm/LoginForm";

type Props = {};

export default function FormContent({}: Props) {
  return (
    <FormContentStyle>
      <h1>Welcome Back</h1>
      <LoginForm />
    </FormContentStyle>
  );
}
