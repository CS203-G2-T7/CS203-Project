import React from "react";
import { LogoMain } from "assets/svgs";
import { LeftSectionStyled } from "./LeftSection.styled";

type Props = {};

export default function LeftSection({}: Props) {
  return (
    <LeftSectionStyled>
      <a href="/landing">
        <LogoMain />
      </a>
      <a href="/landing"><span>OurGardenStory</span></a>
    </LeftSectionStyled>
  );
}
