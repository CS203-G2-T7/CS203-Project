import React from "react";
import { FormNavButtonGroup } from "./FormNavButtonGroup.styled";
import { StyledButton } from "./Button.styled";

type Props = {
  pageSetter: React.Dispatch<React.SetStateAction<number>>;
};

export default function ButtonGroup({ pageSetter }: Props) {
  const backHandler = () => {
    pageSetter((prevPage: number) => {
      if (prevPage > 1) return --prevPage;
      return prevPage;
    });
  };

  const nextHandler = () => {
    pageSetter((prevPage: number) => {
      if (prevPage < 3) return ++prevPage;
      return prevPage;
    });
  };

  return (
    <FormNavButtonGroup>
      <StyledButton
        size="large"
        variant="outlined"
        onClick={() => {
          backHandler();
        }}
      >
        Back
      </StyledButton>
      <StyledButton
        disableElevation
        size="large"
        variant="contained"
        onClick={() => {
          nextHandler();
        }}
      >
        Next
      </StyledButton>
    </FormNavButtonGroup>
  );
}
