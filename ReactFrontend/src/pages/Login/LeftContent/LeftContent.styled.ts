import styled from "styled-components";

export const LeftContentStyled = styled.div`
  position: relative; //so that child form content can be absolute
  /* width: 70%; */
  flex-grow: 1;
  height: 100vh;
  filter: drop-shadow(0.5rem 0px 1.875rem #000);
  background-color: #fefefe;
`;
