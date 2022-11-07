import styled from "styled-components";
import Navbar from "components/Navbar/Navbar";
import React from "react";

type Props = {
  garden: string;
};

const HeaderStyled = styled.div`
  background: linear-gradient(
    89.93deg,
    #1a5119 0.06%,
    rgba(26, 67, 25, 0.85) 99.94%
  );
  font-family: "roboto", sans-serif;

  h1 {
    font-size: 2.5rem;
    color: #fefefe;
    font-weight: 400;
    padding-top: 3rem;
    margin-left: 3rem;
  }
  h2 {
    font-size: 2rem;
    color: #9f9f9f;
    font-weight: 400;
    margin: 0;
    margin-left: 3rem;
    padding-bottom: 5rem;
  }
`;

export default function Header({ garden }: Props) {
  return (
    <HeaderStyled>
      <Navbar />
      <h1>Payment</h1>
      <h2>{garden}</h2>
    </HeaderStyled>
  );
}
