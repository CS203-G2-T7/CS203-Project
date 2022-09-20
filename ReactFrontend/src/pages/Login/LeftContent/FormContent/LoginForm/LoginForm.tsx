import { RedErrorCircle, ViewPWIcon } from "assets/svgs";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { EmailFieldStyled } from "./EmailField.styled";
import HelperText from "./HelperText/HelperText";
import { LoginFormStyled } from "./LoginForm.styled";
import { PasswordFieldStyled } from "./PasswordField.styled";
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
      <EmailFieldStyled valid={errors.email == null}>
        <label>Email</label>
        <input
          {...register("email", {
            required: "Email is required.",
            pattern: {
              value: /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/,
              message: "Not a valid email.",
            },
            max: {
              value: 320,
              message: "Maximimum number of characters 320.",
            },
          })}
          placeholder="Enter your email"
        />
      </EmailFieldStyled>
      <PasswordFieldStyled valid={errors.password == null}>
        <label>Password</label>
        <input
          type={showPass}
          {...register("password", { required: "Password cannot be empty." })}
          placeholder="Enter your password"
        />
        <ViewPWIconStyled
          onMouseDown={() => {
            setShowPass("text");
          }}
          onMouseUp={() => {
            setShowPass("password");
          }}
        />
      </PasswordFieldStyled>
      <HelperText validPass={errors.password == null} />
      <SubmitInputStyled type="submit" />
      <SignUpCTA />
    </LoginFormStyled>
  );
}
