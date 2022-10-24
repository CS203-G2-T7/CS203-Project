import styled from "styled-components";

export const TabPanelStyled = styled.div`
  h4 {
    font-family: "roboto", sans-serif; //details page the garden details and ballot details is roboto not nunito
    font-weight: 500;
    font-size: 1.2rem;
  }

  li,
  p {
    font-family: "roboto", sans-serif; //details page the garden details and ballot details is roboto not nunito
    font-weight: 400;
    font-size: 1.2rem;
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


export const FAQ = styled.div`
    margin-top: 2.5rem;
`;