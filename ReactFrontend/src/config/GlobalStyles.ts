import { createGlobalStyle } from "styled-components";

export const GlobalStyles = createGlobalStyle`
  //screen sizes less than 1600px will have default font-size of 14px. Else 16px.
  @media screen and (max-width: 1600px) {
    html {
      font-size: 14px;
    }
  }
  
  body {
    margin: 0;
    padding: 0;
    color: black;
    font-family: 'Nunito', sans-serif;
    font-weight: 400;
    font-size: 1rem;
    box-sizing: border-box;
  }
  .stopBodyScroll{
    height: 100%;
    overflow: hidden;
  }
  *, *::before, *::after {
    box-sizing: inherit;
  }
`;
