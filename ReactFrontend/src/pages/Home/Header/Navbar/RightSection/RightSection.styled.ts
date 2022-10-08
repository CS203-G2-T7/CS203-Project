import styled from "styled-components";

export const RightSectionStyled = styled.div`
  /* background-color: blue; */
  display: flex;
  flex-direction: row;
  /* padding: 30px 30px 30px 0px; */

  justify-content: flex-end;
  gap: calc(56rem / 16);

  a {
    font-size: 20;
    align-self: center;
    cursor: pointer;
    color: white;
    text-decoration: none;
    &:hover {
      text-decoration: underline;
    }
  }
`;
