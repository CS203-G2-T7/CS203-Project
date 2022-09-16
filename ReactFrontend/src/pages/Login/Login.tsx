import LeftContent from "./LeftContent/LeftContent";
import RightContent from "./RightContent/RightContent";
import { LoginStyled } from "./Login.styled";

export default function Login() {
  return (
    <LoginStyled>
      <LeftContent />
      <RightContent />
    </LoginStyled>
  );
}
