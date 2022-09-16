import React from "react";
import {
  BottomRight,
  RoundLeaf,
  SharpLeaf,
  SquareLeaf,
  TripleLeaf,
} from "assets/svgs";
import { RoundLeafStyled } from "./RoundLeaf.styled";
import { SharpLeafStyled } from "./SharpLeaf.styled";
import { SquareLeafStyled } from "./SquareLeaf.styled";
import { TripleLeafStyled } from "./TripleLeaf.styled";
import { BottomRightStyled } from "./BottomRight.styled";
import { VectorBackgroundStyled } from "./VectorBackground.styled";

type Props = {};

export default function VectorBackground({}: Props) {
  return (
    <VectorBackgroundStyled>
      <RoundLeafStyled>
        <RoundLeaf />
      </RoundLeafStyled>

      <SharpLeafStyled>
        <SharpLeaf />
      </SharpLeafStyled>

      <SquareLeafStyled>
        <SquareLeaf />
      </SquareLeafStyled>

      <TripleLeafStyled>
        <TripleLeaf />
      </TripleLeafStyled>

      <BottomRightStyled>
        <BottomRight />
      </BottomRightStyled>
      
    </VectorBackgroundStyled>
  );
}
