import React, { ReactNode, useState } from "react";
import FirstTextFields from "./FirstTextFields/FirstTextFields";
import { RightContentStyled } from "./RightContent.styled";

import ButtonGroup from "./ButtonGroup/ButtonGroup";
import LoginLink from "./LoginLink/LoginLink";
import SecondTextFields from "./SecondTextFields/SecondTextFields";
import ThirdTextFields from "./ThirdTextFields/ThirdTextFields";
import { FixedBottom } from "./FixedBottom.styled";

type Props = {};

export default function RightContent({}: Props) {
  const [page, setPage] = useState(1);
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
    <RightContentStyled>
      <h1>Create Account</h1>
      {CurrentPageComponent}
      <FixedBottom>
        <ButtonGroup pageSetter={setPage} />
        <LoginLink />
      </FixedBottom>
    </RightContentStyled>
  );
}
