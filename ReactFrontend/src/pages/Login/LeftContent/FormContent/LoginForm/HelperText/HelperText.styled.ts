import styled from "styled-components";

export const HelperTextStyled = styled.div`
  width: 22.5rem;
  height: 1.125rem;
  font-size: 0.6875rem;

  padding: 0 1rem 0;
  margin-top: 0.5rem;

  display: flex;
  justify-content: space-between;
  align-items: center;

  span {
    &:first-child {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      gap: 0.25rem;
      color: #b73b3b;
    }

    &:last-child {
      color: #3c72db;
      font-weight: 500;
      cursor: pointer;
    }
  }
`;
