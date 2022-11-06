import axios, { AxiosResponse } from "axios";
import { prod_url } from "urlConstants";

const GET_ALL_USER_PLANTS_URL = `${prod_url}/my-plant`;
const POST_NEW_PLANT_USER_URL = `${prod_url}/my-plant`;

class Plant {
  getAllUserPlants(): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_ALL_USER_PLANTS_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }

  addPlant(plantName: String): Promise<AxiosResponse<any, any>> {
    return axios.post(
        POST_NEW_PLANT_USER_URL,
      {
        plantName: plantName,
      },
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
        },
      }
    );
  }

}

export default new Plant();
