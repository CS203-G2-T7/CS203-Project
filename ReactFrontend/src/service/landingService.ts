import axios, { AxiosResponse } from "axios";
import { prod_url } from "urlConstants";

const GET_GARDEN_URL = `${prod_url}/garden`;

class Landing {
  checkLoggedIn(): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_GARDEN_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }
}

export default new Landing();
