import styled from "styled-components";

export const LeftSectionStyled = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: calc(8rem / 16); //0.5rem -> 8px
  span {
    font-size: 20;
  }
  a{
    text-decoration: none;
    color: white;
  }
`;
