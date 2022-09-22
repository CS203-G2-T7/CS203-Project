import { RedErrorCircle, ViewPWIcon } from "assets/svgs";
import React, { useEffect, useState } from "react";
import TextField from "@mui/material/TextField";
import { useForm, Controller } from "react-hook-form";
import HelperText from "./HelperText/HelperText";
import { LoginFormStyled } from "./LoginForm.styled";
import SignUpCTA from "./SignUpCTA/SignUpCTA";
import { SubmitInputStyled } from "./SubmitInput.styled";
import { ViewPWIconStyled } from "./ViewPWIcon.styled";

type Props = {};

export type FormDataType = {
  email: string;
  password: string;
};

const defaultFormData: FormDataType = {
  email: "",
  password: "",
};

export default function LoginForm({}: Props) {
  const [showPass, setShowPass] = useState("password");

  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm({
    defaultValues: defaultFormData,
    mode: "onTouched",
  });

  const submitHandler = (data: FormDataType): void => {
    console.log(data);
  };

  return (
    <LoginFormStyled onSubmit={handleSubmit(submitHandler)}>
      <Controller
        name={"email"}
        control={control}
        rules={{
          required: "Email is required.",
          pattern: {
            value: /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/,
            message: "Not a valid email.",
          },
          max: {
            value: 320,
            message: "Maximimum number of characters 320.",
          },
        }}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <TextField
            error={error != null}
            onChange={onChange}
            value={value}
            label={"Email"}
            inputRef={ref}
            fullWidth
            margin="normal"
            placeholder="name@email.com"
          />
        )}
      />
      <Controller
        name={"password"}
        control={control}
        rules={{ required: "Password cannot be empty." }}
        render={({
          field: { onChange, onBlur, value, name, ref },
          fieldState: { isTouched, isDirty, error },
        }) => (
          <TextField
            error={error != null}
            onChange={onChange}
            value={value}
            label={"Password"}
            inputRef={ref}
            type={showPass}
            fullWidth
            margin="normal"
            InputProps={{
              endAdornment: (
                <ViewPWIconStyled
                  onMouseDown={() => {
                    setShowPass("text");
                  }}
                  onMouseUp={() => {
                    setShowPass("password");
                  }}
                />
              ),
            }}
          />
        )}
      />
      <HelperText validPass={errors.password == null} />
      <SubmitInputStyled type="submit" />
      <SignUpCTA />
    </LoginFormStyled>
  );
}
