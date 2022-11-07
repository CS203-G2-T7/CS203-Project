import axios, { AxiosResponse } from "axios";
import { prod_url } from "urlConstants";

const GET_GARDEN_BY_NAME_URL = `${prod_url}/garden?name=`;
const GET_ALL_GARDEN_URL = `${prod_url}/garden`;

class AllGarden {
  //gets Window, which contains array of Gardens
  getAllGarden(): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_ALL_GARDEN_URL);
  }
}

export default new AllGarden();
