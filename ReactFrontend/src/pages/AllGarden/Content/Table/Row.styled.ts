import { Link } from "react-router-dom";
import styled from "styled-components";

export const RowStyled = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  font-size: 1.25rem;
  color: #00131e;
  border-bottom: solid 1px #cccccc;
  text-decoration: none;

  /* &:hover {
    background-color: rgb(0, 0, 0, 0.05);
    transition: all 0.2s ease;
  } */

  p {
    margin: 0;
    padding: 0 1rem;
    &:nth-child(2) {
      flex: 5;
    }
    &:nth-child(3) {
      flex: 5;
    }
    &:nth-child(4) {
      flex: 5;
    }
  }
`;
