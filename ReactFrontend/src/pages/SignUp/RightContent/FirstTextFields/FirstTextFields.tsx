import { InputAdornment, TextField } from "@mui/material";
import React from "react";
import { StyledFirstTextFields } from "./FirstTextField.styled";
import { StyledNameTextFields } from "./NameTextField.styled";
import { StyledNameTextFieldsRow } from "./NameTextFieldRow.styled";
import { Dayjs } from "dayjs";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { Controller, useFormContext } from "react-hook-form";

type Props = {};

export default function FirstTextFields({}: Props) {
  const [dateValue, setDateValue] = React.useState<Dayjs | null>(null);
  // dayjs("2022-10-1T00:00:00")
  const { control, setValue } = useFormContext();

  const handleChange = (newValue: Dayjs | null) => {
    setDateValue(newValue);
    setValue("dateOfBirth", newValue?.format("MM-DD-YYYY"));
  };

  return (
    <StyledFirstTextFields>
      <StyledNameTextFieldsRow>
        <Controller
          name={"firstName"}
          control={control}
          rules={{
            required: "First name is required.",
          }}
          render={({
            field: { onChange, onBlur, value, name, ref },
            fieldState: { isTouched, isDirty, error },
          }) => (
            <StyledNameTextFields
              label="First name"
              onChange={onChange}
              value={value}
              inputRef={ref}
              error={!!error}
              helperText={error?.message}
            />
          )}
        />
        <Controller
          name={"lastName"}
          control={control}
          render={({
            field: { onChange, onBlur, value, name, ref },
            fieldState: { isTouched, isDirty, error },
          }) => (
            <StyledNameTextFields
              label="Last name"
              onChange={onChange}
              value={value}
              inputRef={ref}
              error={!!error}
              helperText={error?.message}
            />
          )}
        />
      </StyledNameTextFieldsRow>

      <Controller
        name={"username"}
        control={control}
        rules={{
          required: "Username is required.",
          pattern: {
            value: /^[a-z0-9_\-]+$/,
            message: "Username must be lowercase characters."
          }
        }}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <StyledNameTextFields
            label="Username"
            onChange={onChange}
            value={value}
            inputRef={ref}
            error={!!error}
            helperText={error?.message}
          />
        )}
      />

      <Controller
        name={"email"}
        control={control}
        rules={{
          required: "Email is required.",
          pattern: {
            value:
              /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
            message: "Invalid email.",
          },
        }}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <TextField
            label="Email"
            onChange={onChange}
            value={value}
            inputRef={ref}
            error={!!error}
            helperText={error?.message}
          />
        )}
      />
      <StyledNameTextFieldsRow>
        <Controller
          name={"phoneNumber"}
          control={control}
          rules={{
            required: "Phone number is required.",
            pattern: {
              value: /^[0-9]+$/,
              message: "Not a valid phone number.",
            },
          }}
          render={({
            field: { onChange, onBlur, value, name, ref },
            fieldState: { isTouched, isDirty, error },
          }) => (
            <StyledNameTextFields
              label="Phone Number"
              onChange={onChange}
              value={value}
              inputRef={ref}
              error={!!error}
              helperText={error?.message}
              type="text"
              inputMode="numeric"
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">+65</InputAdornment>
                ),
              }}
              inputProps={{ maxLength: 8 }}
            />
          )}
        />
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          <Controller
            name={"dateOfBirth"}
            control={control}
            rules={{
              required: "Date of birth is required.",
            }}
            render={({
              field: { onChange, onBlur, name, ref },
              fieldState: { isTouched, isDirty, error },
            }) => (
              <DatePicker
                label="Date of Birth"
                value={dateValue}
                inputRef={ref}
                onChange={(newValue) => {
                  handleChange(newValue);
                }}
                renderInput={(params) => <StyledNameTextFields {...params} />}
              />
            )}
          />
        </LocalizationProvider>
      </StyledNameTextFieldsRow>
    </StyledFirstTextFields>
  );
}
