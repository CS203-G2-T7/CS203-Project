import * as React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import { CardActionArea } from "@mui/material";
import { ActionAreaStyled } from "./ActionArea.styled";

export default function ActionAreaCard() {
  return (
    <Card sx={{}} variant="outlined">
      <CardActionArea>
        <ActionAreaStyled>
          <img src={require("assets/imgs/LettuceImage.jpg")} alt="Lettuce Image" />
          <CardContent>
            <Typography gutterBottom variant="h6" component="div">
              Lettuce
            </Typography>
            <Typography variant="body1" color="text.secondary">
              letucsius gerloris
            </Typography>
          </CardContent>
        </ActionAreaStyled>
      </CardActionArea>
    </Card>
  );
}
