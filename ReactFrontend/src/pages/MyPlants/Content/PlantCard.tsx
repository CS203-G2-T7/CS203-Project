import * as React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import { CardActionArea } from "@mui/material";
import { PlantCardStyled } from "./PlantCard.styled";
import { rowObject } from "../MyPlants";

type Props = {
  rowObject: rowObject;
};

export default function PlantCard({ rowObject }: Props) {
  return (
    <PlantCardStyled>
      <Card sx={{ maxWidth: 400, height:280}}>
        <CardActionArea>
          <CardMedia
            component="img"
            height="180"
            image={require("assets/imgs/LettuceImage.jpg")}
            alt="Lettuce Image"
          />

          <CardContent>
            <Typography gutterBottom variant="h5" component="div">
              {rowObject.plantName}
            </Typography>
            <Typography variant="body1" color="text.secondary">
              {rowObject.species}
            </Typography>
          </CardContent>
        </CardActionArea>
      </Card>
    </PlantCardStyled>
  );
}
