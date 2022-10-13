import styled from "styled-components";

export const DetailsStyled = styled.div`
    ul{
        li{
            font-family:"nunito", serif; //details page the garden details and ballot details is roboto not nunito
            font-weight: bold;
            font-size: 1.2rem;
            p,li{
                font-weight: lighter;
                font-size: 1rem;
            }
            /* a{
                font-size: smaller;
                color: 
            } */

        }
        
    }
    a{
        color: #2E7D32;
        text-decoration: none;
        &:hover {
            color: #2E7D32;
            text-decoration: underline;
        }
    }

`;
