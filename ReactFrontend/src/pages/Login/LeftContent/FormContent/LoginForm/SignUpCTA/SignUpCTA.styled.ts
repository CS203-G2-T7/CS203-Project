import styled from "styled-components";

export const SignUpCTAStyled = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 4px;
  span {
    font-size: 12px;

    &:last-child {
      color: #3c72db;
      font-weight: 500;
      cursor: pointer;
    }
  }
`;
