import styled from "styled-components";

export const RowStyled = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  font-size: 1.25rem;
  color: #00131e;
  border-bottom: solid 1px #cccccc;

  p {
    margin: 0;
    padding: 0 1.923%;
    &:nth-child(2) {
      width: 20%;
    }
    &:nth-child(3) {
      width: 20%;
    }
    &:nth-child(4) {
      width: 20%;
    }
  }
`;
