import styled from "styled-components";
import { animated } from "react-spring";

export const PlantModalStyled = styled(animated.div)`
  position: fixed;
  border-radius: 1rem;
  bottom: 10%;
  background-color: white;
  height: 70%;
  width: 40%;
  z-index: 20;

  left: 30%;
  right: 30%;

  font-family: "inter", sans-serif;
  font-weight: 400;
  color: #00131e;

  div:nth-child(1) {
    height: 50%;
    overflow: hidden;
    margin: 0;
    img {
      width: 100%;
      object-fit: contain;
    }
  }

  div:nth-child(2) {
    padding: 2rem;
    h1 {
      margin: 0;
      font-size: 2.5rem;
    }
    h2 {
      margin-top: 0.3rem;
      font-size: 1.2rem;
      font-weight: 400;
      color: #afafaf;
    }
    p {
      line-height: 158%;
    }
  }
`;
