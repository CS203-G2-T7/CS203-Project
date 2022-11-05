import styled from "styled-components";

export const HeaderRowStyled = styled.div`
  display: flex;
  flex-direction: row;
  /* justify-content: space-between; */
  align-items: center;

  font-size: 1.5rem;
  color: #00131e;
  border-bottom: 2px solid #c7c7c7;

  div {
    padding: 1rem;
  }
  div:nth-child(1) {
    flex: 10;
  }
  div:nth-child(2) {
    flex: 5;
  }
  div:nth-child(3) {
    flex: 5;
  }
  div:nth-child(4) {
    flex: 5;
  }
`;
