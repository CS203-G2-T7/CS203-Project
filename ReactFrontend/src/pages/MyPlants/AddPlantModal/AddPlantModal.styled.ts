import styled from "styled-components";
import { animated } from "react-spring";

export const AddPlantModalStyled = styled(animated.div)`
  position: fixed;
  border-radius: 1rem;
  bottom: 20%;
  background-color: white;
  height: 50%;
  width: 30%;
  z-index: 20;

  left: 35%;
  right: 35%;

  h4 {
    font-family: "Nunito", sans-serif;
    font-weight: 600;
    font-size: 2rem;
    text-align: center;
    margin: 1.5rem;
    color: #00131e;
  }

  ul {
    overflow: auto;
    &::-webkit-scrollbar {
      display: none;
    }

    padding: 0;
    margin: 0;
    height: 75%;
  }
`;
