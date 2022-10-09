import React from "react";
import { FormNavButtonGroup } from "./FormNavButtonGroup.styled";
import { StyledButton } from "./Button.styled";

type Props = {
  pageSetter: React.Dispatch<React.SetStateAction<number>>;
  page: number;
};

export default function ButtonGroup({ pageSetter, page }: Props) {
  const submitState: boolean = page === 3;

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
        disabled={page === 1}
      >
        Back
      </StyledButton>
      <StyledButton
        size="large"
        variant="outlined"
        onClick={() => {
          nextHandler();
        }}
      >
        Next
      </StyledButton>
      <StyledButton
        disableElevation
        size="large"
        variant="contained"
        type="submit"
      >
        Submit
      </StyledButton>
    </FormNavButtonGroup>
  );
}
