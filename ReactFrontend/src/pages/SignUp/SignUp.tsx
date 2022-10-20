import LeftContent from "./LeftContent/LeftContent";
import SignUpFormComponent from "./RightContent/SignUpFormComponent";
import { SignUpStyled } from "./SignUp.styled";

export default function Login() {
  return (
    <SignUpStyled>
      <LeftContent />
      <SignUpFormComponent />
    </SignUpStyled>
  );
}
