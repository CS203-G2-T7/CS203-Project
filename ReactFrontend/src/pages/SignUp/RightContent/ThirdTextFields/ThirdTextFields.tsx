import { TextField } from "@mui/material";
import React from "react";
import { StyledThirdTextFields } from "./ThirdTextFields.styled";
import { StyledNameTextFields } from "./NameTextField.styled";

type Props = {};

export default function ThirdTextFields({}: Props) {
  return (
    <StyledThirdTextFields>
      <TextField label="Choose Password" />
      <TextField label="Confirm Password" />
    </StyledThirdTextFields>
  );
}
