import axios from "axios";
import { prod_url } from "urlConstants";

const LOGIN_BASE_URL = `${prod_url}/api/users/sign-in`;

export interface LoginData {
  username: String;
  password: String;
}

class loginUser {
  loginUser(loginData: LoginData) {
    return axios.post(LOGIN_BASE_URL, loginData);
  }
}

// TODO: Something like this to store JWT
// axios
// .post('http://localhost:8000/logIn', {
//   email: email,
//   password: password,
// })
// .then((response) => {
//   const users = response.data
//     dispatch(loginSuccess(users))
//     localStorage.setItem('jwt', users.auth_token)
//     localStorage.setItem('user', JSON.stringify(users))
//     console.log('users', users) // undefined
// })
// .catch((error) => {
//   dispatch(fetchLoginFailure(error.message))
// })
export default new loginUser();
