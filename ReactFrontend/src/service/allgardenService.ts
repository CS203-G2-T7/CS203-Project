import axios, { AxiosResponse } from "axios";
const GET_GARDEN_BY_NAME_URL = "http://localhost:5000/garden?name=";
const GET_ALL_GARDEN_URL = "http://localhost:5000/garden";




class AllGarden {
  //gets Window, which contains array of Gardens
  getAllGarden(): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_ALL_GARDEN_URL);
  }
}

export default new AllGarden();