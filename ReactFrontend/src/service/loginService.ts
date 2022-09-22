import axios from "axios";

const LOGIN_BASE_URL = "http://localhost:5000/api/users/sign-in";

export interface LoginData {
  username: String;
  password: String;
}

class loginUser {
  loginUser(loginData: LoginData) {
    return axios.post(LOGIN_BASE_URL, loginData);
  }
}

export default new loginUser();
