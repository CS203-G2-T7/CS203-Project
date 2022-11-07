import axios, { AxiosResponse } from "axios";
// import { prod_url } from "urlConstants";

const GET_ALL_USERS_BY_GARDEN_URL = "http://localhost:5000/community";

class Community {
  getAllUsersByGarden(): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_ALL_USERS_BY_GARDEN_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }
}

export default new Community();
