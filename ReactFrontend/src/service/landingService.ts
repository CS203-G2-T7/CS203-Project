import axios, { AxiosResponse } from "axios";
import { prod_url } from "urlConstants";

const GET_WINDOW_URL = `${prod_url}/window`;

class Landing {
  checkLoggedIn(): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_WINDOW_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }
}

export default new Landing();
