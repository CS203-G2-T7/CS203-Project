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
        <a href="https://www.nparks.gov.sg/-/media/nparks-real-content/gardening/allotment-gardening/allotment-gardens-faqs_mar-2022.ashx?la=en&hash=2955B790F8BA94458309D0E575D130EEB6BEA661&hash=2955B790F8BA94458309D0E575D130EEB6BEA661">
          <FacebookIcon />
        </a>
        <InstagramIcon />
        <TwitterIcon />
      </IconStyled>
      <p>#nparksbuzz</p>
    </SocialMediaStyled>
  );
}
