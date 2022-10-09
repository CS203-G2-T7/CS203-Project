import styled from "styled-components";

export const WindowLabelStyled = styled.div`
  height: calc(84rem/16);
  div{
    display:flex;
    align-items: center; //align content is within the elements itself
    justify-content: flex-start;
    //gap: 0.5rem;
    h1{
      margin: 0;//set margin to zero first then margin bottom, the default will still apply
      font-size: calc(40rem/16);
    }
    margin-bottom: calc(18rem/16);
  }
    

  p{
    margin:0;
    font-size: 1rem;
    color:#999999;
  }
  margin-bottom:calc(52rem/16);
`;
