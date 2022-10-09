import styled from "styled-components";

export const LoginLinkStyled = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.25rem;

  font-size: 0.75rem;
  margin-top: 2rem;

  a:last-child:hover,
  a:last-child:visited,
  a:last-child:link,
  a:last-child:active {
    cursor: pointer;
    text-decoration: none;
    color: #3c72db;
  }
`;
