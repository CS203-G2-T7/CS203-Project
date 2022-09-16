import styled from "styled-components";

type Props = {
  valid: boolean;
};

export const PasswordFieldStyled = styled.div<Props>`
  position: relative;
  label {
    font-size: 14px;
    display: block;
    padding-left: 16px;
    padding-bottom: 8px;
  }

  input {
    width: 360px;
    height: 56px;
    background-color: ${(Props) => (Props.valid ? "transparent" : "#fbeaea")};
    border-width: 1px;
    border-style: solid;
    border-color: ${(Props) => (Props.valid ? "#c6c6c6" : "#d32f2f")};
    border-radius: 16px;
    padding-left: 16px;
    font-size: 16px;
    font-family: "roboto", sans-serif;

    &::placeholder {
      font-size: 16px;
      color: #cbcbcb;
      transform: translateY(0px);
    }
    &:focus {
      outline: none;
      border-color: ${(Props) => (Props.valid ? "#000000" : "#d32f2f")};
      border-style: solid;
      border-width: 1px;
      &::placeholder {
        color: transparent;
      }
    }
  }
`;
