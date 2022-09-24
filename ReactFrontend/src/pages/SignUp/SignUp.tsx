import LeftContent from "./LeftContent/LeftContent";
import RightContent from "./RightContent/RightContent";
import { SignUpStyled } from "./SignUp.styled";

export default function Login() {
  return (
    <SignUpStyled>
      <LeftContent />
      <RightContent />
    </SignUpStyled>
  );
}
