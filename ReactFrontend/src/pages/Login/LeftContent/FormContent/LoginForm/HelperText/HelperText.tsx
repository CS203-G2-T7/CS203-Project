import { RedErrorCircle } from "assets/svgs";
import React from "react";
import { HelperTextStyled } from "./HelperText.styled";

type Props = {
  validPass: boolean;
};

export default function HelperText({ validPass }: Props) {
  return (
    <HelperTextStyled>
      {validPass ? (
        <i />
      ) : (
        <span>
          <RedErrorCircle />
          Wrong password
        </span>
      )}
      <span>Forgot password?</span>
    </HelperTextStyled>
  );
}
