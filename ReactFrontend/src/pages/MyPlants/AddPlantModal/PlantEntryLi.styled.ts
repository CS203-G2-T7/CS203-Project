import styled from "styled-components";

export const PlantEntryLiStyled = styled.li`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  list-style: none;
  width: 91.3%;
  height: 3.6875rem;
  margin: auto;
  border-bottom: 0.0625rem solid #e8e8e8;
  font-weight: 400;
  &:last-child {
    border-bottom: unset;
    margin-bottom: 1.25rem;
  }
`;
