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
  ul {
    padding: 0;
    margin: 0;
    margin-top: 1.25rem;
    height: fit-content;
    max-height: 70vh;
  }
`;
