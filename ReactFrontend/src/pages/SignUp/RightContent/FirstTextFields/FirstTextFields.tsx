import { TextField } from "@mui/material";
import React from "react";
import { StyledFirstTextFields } from "./FirstTextField.styled";
import { StyledNameTextFields } from "./NameTextField.styled";
import { StyledNameTextFieldsRow } from "./NameTextFieldRow.styled";
import { Dayjs } from "dayjs";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

type Props = {};

export default function FirstTextFields({}: Props) {
  const [value, setValue] = React.useState<Dayjs | null>(null);
  // dayjs("2022-10-1T00:00:00")

  const handleChange = (newValue: Dayjs | null) => {
    setValue(newValue);
  };

  return (
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
            handleChange(newValue);
          }}
          renderInput={(params) => <StyledNameTextFields {...params} />}
        />
      </LocalizationProvider>
    </StyledFirstTextFields>
  );
}
