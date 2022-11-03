import React, { useState } from "react";
import TextField from "@mui/material/TextField";
import { useForm, Controller } from "react-hook-form";
import HelperText from "./HelperText/HelperText";
import { LoginFormStyled } from "./LoginForm.styled";
import SignUpCTA from "./SignUpCTA/SignUpCTA";
import { SubmitInputStyled } from "./SubmitInput.styled";
import { ViewPWIconStyled } from "./ViewPWIcon.styled";
import loginService, { LoginData } from "service/loginService";
import { useNavigate } from "react-router-dom";

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
  const navigate = useNavigate();
  const {
    handleSubmit,
    control,
    formState: { errors },
  } = useForm({
    defaultValues: defaultFormData,
    mode: "onTouched",
  });

  const submitHandler = (data: FormDataType): void => {
    loginService
      .loginUser({
        username: data.email,
        password: data.password,
      })
      .then((res) => {
        console.log(res);
        localStorage.setItem("jwtAccessToken", res.data.accessToken);
        navigate("/landing");
        console.log(res.data.accessToken);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <LoginFormStyled onSubmit={handleSubmit(submitHandler)}>
      <Controller
        name={"email"}
        control={control}
        rules={{
          required: "Username is required.",
          max: {
            value: 320,
            message: "Maximum number of characters 320.",
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
            label={"Username"}
            inputRef={ref}
            fullWidth
            margin="normal"
            placeholder="e.g John Tan"
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
      <SubmitInputStyled type="submit" variant="contained" disableElevation>
        Login
      </SubmitInputStyled>
      <SignUpCTA />
    </LoginFormStyled>
  );
}
