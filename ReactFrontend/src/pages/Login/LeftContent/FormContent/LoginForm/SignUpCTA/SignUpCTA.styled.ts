import styled from "styled-components";

export const SignUpCTAStyled = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 0.25rem;
  span {
    font-size: 0.75rem;
  }
  a:hover,
  a:visited,
  a:link,
  a:active {
    cursor: pointer;
    text-decoration: none;
    color: #3c72db;
    font-weight: 500;
  }
`;
