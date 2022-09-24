import styled from "styled-components";

export const SignUpCTAStyled = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  gap: 4px;
  span {
    font-size: 12px;
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
