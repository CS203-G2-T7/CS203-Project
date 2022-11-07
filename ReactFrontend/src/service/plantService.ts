import axios, { AxiosResponse } from "axios";
import { prod_url } from "urlConstants";

const USER_PLANTS_URL = `${prod_url}/my-plant`;
const ALL_PLANTS_URL = `${prod_url}/plant`;

class Plant {
  getAllUserPlants(): Promise<AxiosResponse<any, any>> {
    return axios.get(USER_PLANTS_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }

  getAllPlants(): Promise<AxiosResponse<any, any>> {
    return axios.get(ALL_PLANTS_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }

  addPlant(plantName: String): Promise<AxiosResponse<any, any>> {
    const requestBody = [
      {
        plantName: plantName,
      },
    ];
    return axios.post(USER_PLANTS_URL, requestBody, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }
}

export default new Plant();
