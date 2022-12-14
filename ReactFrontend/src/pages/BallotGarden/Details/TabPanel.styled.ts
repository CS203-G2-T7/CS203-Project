import styled from "styled-components";

export const TabPanelStyled = styled.div`
  h4 {
    font-family: "roboto", sans-serif; //details page the garden details and ballot details is roboto not nunito
    font-weight: 500;
    font-size: 1.2rem;
    margin-bottom: 0rem;
  }

  li,
  p {
    font-family: "roboto", sans-serif; //details page the garden details and ballot details is roboto not nunito
    font-weight: 400;
    font-size: 1.2rem;
    margin-top: 0.5rem;
    
  }

  a {
    color: #2e7d32;
    text-decoration: none;
    &:hover {
      color: #2e7d32;
      text-decoration: underline;
    }
  }
`;

export const VegListStyled = styled.p`
  padding-left: 2rem;
  font-style: italic;
  padding-bottom: calc(11.2rem / 16);
  margin:0;
`;
