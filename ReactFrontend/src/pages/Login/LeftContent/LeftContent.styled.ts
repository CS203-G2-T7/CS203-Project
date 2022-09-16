import styled from "styled-components";

export const LeftContentStyled = styled.div`
  position: relative; //so that child form content can be absolute
  /* width: 70%; */
  flex-grow: 1;
  height: 100vh;
  filter: drop-shadow(8px 0px 30px #000);
  background-color: #fefefe;
`;
