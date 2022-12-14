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
  margin-bottom: 5rem;
  p {
    margin: 0;
    margin-top: 4rem;  
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
  margin-top: 1rem;
`;

export const IconStyled = styled.div`
  align-items: center;
  display: flex;
  gap: 5rem;
  a {
    cursor: pointer;
  }
  margin-top: 2rem;
  margin-bottom: 2rem;
`;
