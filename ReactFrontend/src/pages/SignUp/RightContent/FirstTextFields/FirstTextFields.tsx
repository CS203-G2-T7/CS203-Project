import { Button, createTheme, TextField, ThemeProvider } from "@mui/material";
import React from "react";
import { StyledButton } from "./Button.styled";
import { StyledFirstTextFields } from "./FirstTextField.styled";
import { FormNavButtonGroup } from "./FormNavButtonGroup.styled";
import { StyledNameTextFields } from "./NameTextField.styled";
import { StyledNameTextFieldsRow } from "./NameTextFieldRow.styled";

const theme = createTheme({
  palette: {
    primary: {
      // Purple and green play nicely together.
      main: "#2E7D32",
    },
    secondary: {
      // This is green.A700 as hex.
      main: "#BCBCBc",
    },
  },
});

type Props = {};

export default function FirstTextFields({}: Props) {
  return (
    <>
      <StyledFirstTextFields>
        <StyledNameTextFieldsRow>
          <StyledNameTextFields label="First name" />
          <StyledNameTextFields label="Last name" />
        </StyledNameTextFieldsRow>
        <TextField label="Email" />
        <TextField label="Date of Birth" />
      </StyledFirstTextFields>
      <ThemeProvider theme={theme}>
        <FormNavButtonGroup>
          <StyledButton size="large" variant="outlined">
            Back
          </StyledButton>
          <StyledButton size="large" variant="contained">
            Next
          </StyledButton>
        </FormNavButtonGroup>
      </ThemeProvider>
    </>
  );
}
