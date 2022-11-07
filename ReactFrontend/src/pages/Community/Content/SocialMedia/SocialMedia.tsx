import { SocialMediaStyled } from "./SocialMedia.styled";
import {
  FacebookIcon,
  InstagramIcon,
  TwitterIcon,
  EmailIcon,
} from "assets/svgs";

type Props = {
  email: string;
};

export default function SocialMedia({ email }: Props) {
  const emailURL = "mailto:" + email;
  return (
    <SocialMediaStyled>
      <a href={emailURL}>
        <EmailIcon />
      </a>

      <a href="https://www.facebook.com/nparksbuzz/">
        <FacebookIcon />
      </a>

      <a href="https://www.instagram.com/nparksbuzz/?hl=en">
        <InstagramIcon />
      </a>

      <a href="https://twitter.com/nparksbuzz?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor">
        <TwitterIcon />
      </a>
    </SocialMediaStyled>
  );
}
