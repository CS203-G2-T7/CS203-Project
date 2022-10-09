import React, { ReactNode, useState } from "react";
import FirstTextFields from "./FirstTextFields/FirstTextFields";
import { RightContentStyled } from "./RightContent.styled";

import ButtonGroup from "./ButtonGroup/ButtonGroup";
import LoginLink from "./LoginLink/LoginLink";
import SecondTextFields from "./SecondTextFields/SecondTextFields";
import ThirdTextFields from "./ThirdTextFields/ThirdTextFields";
import { FixedBottom } from "./FixedBottom.styled";
import { FormProvider, useForm } from "react-hook-form";
import signUpService, {
  defaultSignUpData,
  signUpData,
} from "service/signUpService";

type Props = {};

type SignUpForm = {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword: string;
  addressLine1: string;
  addressLine2: string;
  postalCode: string;
  dateOfBirth: string;
};

const defaultSignUpForm: SignUpForm = {
  firstName: "",
  lastName: "",
  email: "",
  password: "",
  confirmPassword: "",
  addressLine1: "",
  addressLine2: "",
  postalCode: "",
  dateOfBirth: "",
};

export default function RightContent({}: Props) {
  const [page, setPage] = useState(1);
  const methods = useForm({
    defaultValues: defaultSignUpForm,
    mode: "onChange",
  });

  const submitHandler = async (data: SignUpForm) => {
    // console.log(data);
    let signUpData: signUpData = defaultSignUpData;
    signUpData.address = data.addressLine1.concat(" ", data.addressLine2);
    signUpData.email = data.email;
    signUpData.password = data.password;
    signUpData.username = data.firstName.concat(" ", data.lastName);
    // console.log(signUpData);

    signUpService
      .signUpUser(signUpData)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
    // const signUpResponse = await signUpService.signUpUser(signUpData);
    // console.log(signUpResponse);
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
