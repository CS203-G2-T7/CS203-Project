import axios from "axios";

const SIGN_UP_BASE_URL = "http://localhost:5000/api/users/sign-up";

export type signUpData = {
  email: string;
  username: string;
  password: string;
  address: string;
  givenName: string;
  familyName: string;
  birthDate: string;
  phoneNumber: string;
};
export const defaultSignUpData = {
  email: "",
  username: "",
  password: "",
  address: "",
  givenName: "",
  familyName: "",
  birthDate: "",
  phoneNumber: "",
};

class signUpUser {
  signUpUser(signUpData: signUpData) {
    return axios.post(SIGN_UP_BASE_URL, signUpData);
  }
}
export default new signUpUser();
