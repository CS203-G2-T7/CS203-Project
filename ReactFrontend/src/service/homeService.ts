import axios, { AxiosResponse } from "axios";

const LATEST_WINDOW_GARDEN_URL = "http://localhost:5000/window/win1/garden";//hardcoded
const GET_GARDEN_BY_NAME_URL = "http://localhost:5000/garden?name=";
const LATEST_WINDOW_GARDEN_ALLBALLOTS_URL = "http://localhost:5000//window/win1/allBallot";


class Home {
  //gets Window, which contains array of Gardens
  getGardenInLatestWindow(): Promise<AxiosResponse<any, any>> {
    return axios.get(LATEST_WINDOW_GARDEN_URL);
  }

  getGardenByName(gardenName: String): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_GARDEN_BY_NAME_URL + gardenName);
  }

  // getAllBallotsInWindGarden(): Promise<AxiosResponse<any, any>> {
  //   return axios.get(LATEST_WINDOW_GARDEN_ALLBALLOTS_URL, {
  //     headers: {
  //       Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
  //     },
  //   });
  // }

}

export default new Home();
