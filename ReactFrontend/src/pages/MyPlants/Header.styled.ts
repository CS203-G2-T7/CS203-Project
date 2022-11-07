import styled from "styled-components";

export const HeaderStyled = styled.div`
  background-image: url("https://www.ourgardenstory.me/image/myPlantsHeader.png");
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  p {
    font-family: "Roboto", serif;
    font-weight: 400;
    color: white;
    margin: 0;
    padding-left: 5rem;
  }
  p:nth-of-type(1) {
    font-size: calc(38rem / 16);
    padding-top: 5rem;
  }

  p:nth-of-type(2) {
    font-size: calc(28rem / 16);
    padding-top: 1rem;
    padding-bottom: 10rem;
  }
`;
