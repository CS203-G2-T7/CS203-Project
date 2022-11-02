import styled from "styled-components";

export const SocialMediaStyled = styled.div`
  color: #1a5119;
  font-weight: 600;
  font-size: calc(24rem / 16);
  text-align: center;

  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0;
  padding-bottom: 2rem;
  p {
    margin: 0;
    padding-top: 2rem;
  }
  a {
    color: #1a5119;
    text-decoration: none;
  }
  a:hover {
    text-decoration: underline;
  }
`;

export const FollowStyled = styled.div`
  color: black;
`;

export const IconStyled = styled.div`
  align-items: center;
  display: flex;
  padding-top: 1rem;
  gap: 5rem;
  a {
    cursor: pointer;
  }
  margin-top: 2rem;
  margin-bottom: 2rem;
`;
