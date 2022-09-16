import { createGlobalStyle } from "styled-components";

export const GlobalStyles = createGlobalStyle`
  :root {
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
