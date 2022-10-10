import React from "react";
import { TextField } from "@mui/material";
import { StyledThirdTextFields } from "./ThirdTextFields.styled";
import { Controller, useFormContext } from "react-hook-form";

type Props = {};

export default function ThirdTextFields({}: Props) {
  const { control, watch } = useFormContext();

  return (
    <StyledThirdTextFields>
      <Controller
        name={"password"}
        control={control}
        rules={{
          required: { value: true, message: "Password is required." },
          pattern: {
            value: /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{7,15}$/,
            message:
              "Password must be alphanumeric and contain a special character.",
          },
        }}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <TextField
            label="Password"
            onChange={onChange}
            value={value}
            inputRef={ref}
            error={!!error}
            helperText={error?.message}
            type="password"
            inputProps={{ maxLength: 15 }}
          />
        )}
      />
      <Controller
        name={"confirmPassword"}
        control={control}
        rules={{
          validate: (password: string) => {
            if (watch("password") !== password) {
              return "Your passwords do no match";
            }
          },
        }}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <TextField
            label="Confirm Password"
            onChange={onChange}
            value={value}
            inputRef={ref}
            error={!!error}
            helperText={error?.message}
            type="password"
            inputProps={{ maxLength: 15 }}
          />
        )}
      />
    </StyledThirdTextFields>
  );
}
