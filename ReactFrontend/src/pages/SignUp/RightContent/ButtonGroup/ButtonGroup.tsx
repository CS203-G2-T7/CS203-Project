import React from "react";
import { FormNavButtonGroup } from "./FormNavButtonGroup.styled";
import { StyledButton } from "./Button.styled";
import { createTheme, ThemeProvider } from "@mui/material";

type Props = {
  pageSetter: React.Dispatch<React.SetStateAction<number>>;
};

const theme = createTheme({
  palette: {
    primary: {
      // Purple and green play nicely together.
      main: "#2E7D32",
    },
    secondary: {
      // This is green.A700 as hex.
      main: "#BCBCBc",
    },
  },
});

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
    <ThemeProvider theme={theme}>
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
    </ThemeProvider>
  );
}
