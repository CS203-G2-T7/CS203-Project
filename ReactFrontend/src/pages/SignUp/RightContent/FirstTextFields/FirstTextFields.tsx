import { Button, createTheme, TextField, ThemeProvider } from "@mui/material";
import React from "react";
import { StyledButton } from "./Button.styled";
import { StyledFirstTextFields } from "./FirstTextField.styled";
import { FormNavButtonGroup } from "./FormNavButtonGroup.styled";
import { StyledNameTextFields } from "./NameTextField.styled";
import { StyledNameTextFieldsRow } from "./NameTextFieldRow.styled";
import dayjs, { Dayjs } from "dayjs";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

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
  const [value, setValue] = React.useState<Dayjs | null>(null);
  // dayjs("2022-10-1T00:00:00")

  const handleChange = (newValue: Dayjs | null) => {
    setValue(newValue);
  };

  return (
    <>
      <StyledFirstTextFields>
        <StyledNameTextFieldsRow>
          <StyledNameTextFields label="First name" />
          <StyledNameTextFields label="Last name" />
        </StyledNameTextFieldsRow>
        <TextField label="Email" />
        {/* <TextField label="Date of Birth" /> */}
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <DatePicker
            label="Date of Birth"
            value={value}
            onChange={(newValue) => {
              setValue(newValue);
            }}
            renderInput={(params) => <StyledNameTextFields {...params} />}
          />
        </LocalizationProvider>
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
