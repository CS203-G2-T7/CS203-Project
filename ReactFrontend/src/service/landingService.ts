import axios, { AxiosResponse } from "axios";

const GET_WINDOW_URL = "http://localhost:5000/window";

class Landing {
  checkLoggedIn(): Promise<AxiosResponse<any, any>> {
    return axios.get(
        GET_WINDOW_URL,
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem('jwtAccessToken'),
        },
      }
    );
  }
}

export default new Landing();
