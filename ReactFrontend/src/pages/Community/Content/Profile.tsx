import * as React from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import { ProfileStyled } from "./Profile.styled";
import { User } from "../Community";
import SocialMedia from "./SocialMedia/SocialMedia";
import Box from "@mui/material/Box";
import CardActions from "@mui/material/CardActions";
import { Avatar } from "assets/svgs";

type Props = {
  user: User;
};

export default function Profile({ user }: Props) {
  return (
    <ProfileStyled>
      <Avatar />
      <p>{user.sk}</p>
      <SocialMedia email={user.email}/>

      {/* <Card sx={{ maxWidth: 400, height: 268 }}>
        <CardActionArea>
          <CardMedia
            component="img"
            height="180"
            image={require("assets/imgs/Lettuce.jpg")}
            alt="Lettuce Image"
          />

          <CardContent>
            <Typography gutterBottom variant="h5" component="div">
              {user.sk}
            </Typography>
            <Typography variant="body1" color="text.secondary">
              {user.email}
            </Typography>
          </CardContent>
        </CardActionArea>
      </Card> */}
    </ProfileStyled>
  );
}
