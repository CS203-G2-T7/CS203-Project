import React from "react";
import VectorBackground from "./VectorBackground/VectorBackground";
import { LeftContentStyled } from "./VectorBackground/LeftContent.styled";
import FormContent from "./FormContent/FormContent";

type Props = {};

export default function LeftContent({}: Props) {
  return (
    <LeftContentStyled>
      <FormContent/>
      <VectorBackground />
    </LeftContentStyled>
  );
}
