import * as React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import { CardActionArea } from "@mui/material";
import { PlantCardStyled } from "./PlantCard.styled";
import { Plant } from "../MyPlants";

type Props = {
  plant: Plant;
};

export default function PlantCard({ plant }: Props) {
  // console.log(plant);
  if (plant.sk === "") {
    plant.sk = "Lettuce";
  }

  return (
    <PlantCardStyled>
      <Card sx={{ maxWidth: 400, height: 268 }}>
        <CardActionArea>
          <CardMedia
            component="img"
            height="180"
            image={require("assets/imgs/" + plant.sk + ".jpg")}
            alt="Lettuce Image"
          />

          <CardContent>
            <Typography gutterBottom variant="h5" component="div">
              {plant.sk}
            </Typography>
            <Typography variant="body1" color="text.secondary">
              {plant.plantSpecies}
            </Typography>
          </CardContent>
        </CardActionArea>
      </Card>
    </PlantCardStyled>
  );
}
