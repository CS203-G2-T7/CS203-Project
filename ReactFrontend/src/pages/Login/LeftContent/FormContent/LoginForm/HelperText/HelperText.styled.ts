import styled from "styled-components";

export const HelperTextStyled = styled.div`
  width: 360px;
  height: 18px;
  font-size: 11px;

  padding: 0 16px 0;
  margin-top: 8px;

  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    &:first-child {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      gap: 4px;
      color: #b73b3b;
    }

    &:last-child {
      color: #3c72db;
      font-weight: 500;
      cursor: pointer;
    }
  }
`;
