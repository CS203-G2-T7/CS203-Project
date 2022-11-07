import React from "react";
import { LogoMain } from "assets/svgs";
import { LeftSectionStyled } from "./LeftSection.styled";

type Props = {};

export default function LeftSection({}: Props) {
  return (
    <LeftSectionStyled>
      <a href="/">
        <LogoMain />
      </a>
      <a href="/">
        <span>OurGardenStory</span>
      </a>
    </LeftSectionStyled>
  );
}
