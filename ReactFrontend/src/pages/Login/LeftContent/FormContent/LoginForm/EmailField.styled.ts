import styled from "styled-components";

type Props = {
  valid: boolean;
};

export const EmailFieldStyled = styled.div<Props>`
  label {
    font-size: 14px;
    display: block;
    padding-left: 16px;
    padding-bottom: 8px;
  }
  input {
    width: 360px;
    height: 56px;
    border-radius: 16px;
    background-color: ${(Props) => (Props.valid ? "transparent" : "#fbeaea")};
    border-width: 1px;
    border-style: solid;
    border-color: ${(Props) => (Props.valid ? "#c6c6c6" : "#d32f2f")};
    padding-left: 16px;
    margin-bottom: 24px;
    font-size: 16px;
    font-family: "roboto", sans-serif;

    &::placeholder{
      font-size: 16px;
      color: #cbcbcb;
      transform: translateY(0px);
    }
    &:focus {
      /* border: 1px solid black; */
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
