import React, { ReactNode, useState } from "react";
import FirstTextFields from "./FirstTextFields/FirstTextFields";
import { RightContentStyled } from "./RightContent.styled";

import ButtonGroup from "./ButtonGroup/ButtonGroup";
import LoginLink from "./LoginLink/LoginLink";
import SecondTextFields from "./SecondTextFields/SecondTextFields";
import ThirdTextFields from "./ThirdTextFields/ThirdTextFields";
import { FixedBottom } from "./FixedBottom.styled";
import { FormProvider, useForm } from "react-hook-form";
import { Navigate, useNavigate } from "react-router-dom";
import signUpService, {
  defaultSignUpData,
  signUpData,
} from "service/signUpService";
import loginService from "service/loginService";

type Props = {};

type SignUpForm = {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password: string;
  confirmPassword: string;
  phoneNumber: string;
  addressLine1: string;
  addressLine2: string;
  postalCode: string;
  dateOfBirth: string;
};

const defaultSignUpForm: SignUpForm = {
  firstName: "",
  lastName: "",
  email: "",
  username: "",
  password: "",
  confirmPassword: "",
  phoneNumber: "",
  addressLine1: "",
  addressLine2: "",
  postalCode: "",
  dateOfBirth: "",
};

export default function RightContent({}: Props) {
  const navigate = useNavigate();
  const [page, setPage] = useState(1);
  const methods = useForm({
    defaultValues: defaultSignUpForm,
    mode: "onChange",
  });

  const submitHandler = async (data: SignUpForm) => {
    const signUpData: signUpData = {
      email: data.email,
      username: data.username,
      password: data.password,
      address: data.postalCode,
      givenName: data.firstName,
      familyName: data.lastName,
      birthDate: data.dateOfBirth,
      phoneNumber: "+65" + data.phoneNumber, //must have +65 at the front.
    };
    console.log(signUpData);

    try {
      //check if sign up is successful
      const signUpResponse = await signUpService.signUpUser(signUpData);
      console.log(signUpResponse);

      //sign up no issue, then login
      if (signUpResponse.status === 200) {
        const loginResponse = await loginService.loginUser({
          username: signUpData.username,
          password: signUpData.password,
        });
        console.log(loginResponse);
        if (loginResponse.status === 200) {
          localStorage.setItem(
            "jwtAccessToken",
            loginResponse.data.accessToken //JH: bad practice.
          );
          console.log(loginResponse.data.accessToken);
          navigate("/home"); //redirect to home page after sign-up.
        }
      }
    } catch (e) {
      console.log(e);
      alert(e);
    }
  };

  let CurrentPageComponent: ReactNode = <FirstTextFields />;
  switch (page) {
    case 1:
      CurrentPageComponent = <FirstTextFields />;
      break;
    case 2:
      CurrentPageComponent = <SecondTextFields />;
      break;
    case 3:
      CurrentPageComponent = <ThirdTextFields />;
      break;
    default:
      CurrentPageComponent = <FirstTextFields />;
      break;
  }

  return (
    <FormProvider {...methods}>
      <RightContentStyled onSubmit={methods.handleSubmit(submitHandler)}>
        <div>
          <h1>Create Account</h1>
          {CurrentPageComponent}
        </div>
        <FixedBottom>
          <ButtonGroup pageSetter={setPage} page={page} />
          <LoginLink />
        </FixedBottom>
      </RightContentStyled>
    </FormProvider>
  );
}
