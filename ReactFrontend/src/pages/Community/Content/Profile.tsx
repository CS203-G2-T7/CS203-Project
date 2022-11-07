import * as React from "react";
import { ProfileStyled } from "./Profile.styled";
import { User } from "../Community";
import SocialMedia from "./SocialMedia/SocialMedia";
import { Avatar } from "assets/svgs";

type Props = {
  user: User;
};

export default function Profile({ user }: Props) {
  return (
    <ProfileStyled>
      <Avatar />
      <p>{user.userName}</p>
      <SocialMedia email={user.email}/>
    </ProfileStyled>
  );
}
