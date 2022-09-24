import styled from "styled-components";

export const LoginLinkStyled = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;

  font-size: 12px;
  margin-top: 32px;

  a:last-child:hover,
  a:last-child:visited,
  a:last-child:link,
  a:last-child:active {
    cursor: pointer;
    text-decoration: none;
    color: #3c72db;
  }
`;
