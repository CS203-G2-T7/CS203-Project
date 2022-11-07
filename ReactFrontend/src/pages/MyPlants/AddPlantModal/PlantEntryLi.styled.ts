import styled from "styled-components";

export const PlantEntryLiStyled = styled.li`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  list-style: none;

  width: 80%;
  height: 4rem;
  margin: auto;
  padding-left: 1rem;

  border-bottom: 0.0625rem solid #e8e8e8;

  font-weight: 400;
  font-family: "inter", sans-serif;
  color: #00131e;
  background-color: #ffffff;

  cursor: pointer;

  &:last-child {
    border-bottom: unset;
    margin-bottom: 1.25rem;
  }

  &:hover {
    filter: brightness(98%);
  }
`;
