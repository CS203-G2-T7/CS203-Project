import {
  SocialMediaStyled,
  FollowStyled,
  IconStyled,
} from "./SocialMedia.styled";

import React from "react";
import { FacebookIcon, InstagramIcon, TwitterIcon } from "assets/svgs";

type Props = {};

export default function Landing() {
  return (
    <SocialMediaStyled>
      <p>Connect With Us</p>
      <FollowStyled>
        Follow us on social media to see what we're up to and join in our
        activities!
      </FollowStyled>
      <IconStyled>
        <a href="https://www.facebook.com/nparksbuzz/">
          <FacebookIcon />
        </a>

        <a href="https://www.instagram.com/nparksbuzz/?hl=en">
          <InstagramIcon />
        </a>

        <a href="https://twitter.com/nparksbuzz?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor">
          <TwitterIcon />
        </a>
      </IconStyled>
      
      <a href="https://www.instagram.com/explore/tags/nparksbuzz/?hl=en">
        #nparksbuzz
      </a>
    </SocialMediaStyled>
  );
}
