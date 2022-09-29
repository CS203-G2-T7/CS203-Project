import { randomFillSync } from "crypto";
import React from "react";
import { TransformStream } from "stream/web";
import { RightSectionStyled } from "./RightSection.styled";

type Props = {};

export default function RightSection({}: Props) {
  return (
    <RightSectionStyled>
      <div>
        whatever
      </div>

      <div>
        whatever
      </div>

      <div>
        whatever
      </div>
    </RightSectionStyled>
  );
}

