import { TextField } from "@mui/material";
import React from "react";
import { StyledSecondTextFields } from "./SecondTextFields.styled";
import { StyledNameTextFields } from "./NameTextField.styled";

type Props = {};

export default function SecondTextFields({}: Props) {
  return (
    <StyledSecondTextFields>
      <TextField label="Address line 1" />
      <TextField label="Address line 2" />
      <StyledNameTextFields label="Postal code" />
    </StyledSecondTextFields>
  );
}
