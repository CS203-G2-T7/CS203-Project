import { InputAdornment, TextField } from "@mui/material";
import React from "react";
import { StyledSecondTextFields } from "./SecondTextFields.styled";
import { StyledNameTextFields } from "./NameTextField.styled";
import { Controller, useFormContext } from "react-hook-form";

type Props = {};

export default function SecondTextFields({}: Props) {
  const { control } = useFormContext();
  return (
    <StyledSecondTextFields>
      <Controller
        name={"addressLine1"}
        control={control}
        rules={{
          required: "Address is required.",
        }}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <TextField
            label="Address line 1"
            onChange={onChange}
            value={value}
            inputRef={ref}
            error={!!error}
            helperText={error?.message}
          />
        )}
      />
      <Controller
        name={"addressLine2"}
        control={control}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <TextField
            label="Address line 2"
            onChange={onChange}
            value={value}
            inputRef={ref}
            error={!!error}
            helperText={error?.message}
            placeholder={"Optional"}
          />
        )}
      />
      <Controller
        name={"postalCode"}
        control={control}
        rules={{
          required: "Postal code is required.",
          pattern: { value: /^[0-9]+$/, message: "Not a valid postal code." },
        }}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <StyledNameTextFields
            label="Postal code"
            onChange={onChange}
            value={value}
            inputRef={ref}
            error={!!error}
            helperText={error?.message}
            type="text"
            inputMode="numeric"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">S</InputAdornment>
              ),
            }}
            inputProps={{ maxLength: 6 }}
          />
        )}
      />
    </StyledSecondTextFields>
  );
}
