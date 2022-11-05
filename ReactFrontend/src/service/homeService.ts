import axios, { AxiosResponse } from "axios";

const LATEST_WINDOW_GARDEN_URL = "http://localhost:5000/window/win1/garden";//hardcoded
const GET_WINDOW_BY_ID_URL  = "http://localhost:5000/window?id=win1";
const LATEST_WINDOW_GARDEN_ALLBALLOTS_URL = "http://localhost:5000/window/win1/allBallot";
const GET_GARDEN_BY_NAME_URL = "http://localhost:5000/garden?name=";

class Home {
  //gets Window, which contains array of Gardens
  getGardenInLatestWindow(): Promise<AxiosResponse<any, any>> {
    return axios.get(LATEST_WINDOW_GARDEN_URL);
  }

  getLatestWindow(): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_WINDOW_BY_ID_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }


  getAllBallotsInWindGarden(gardenName: String): Promise<AxiosResponse<any, any>> {
    return axios.get(LATEST_WINDOW_GARDEN_ALLBALLOTS_URL, );
  }


  getGardenByName(gardenName: String): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_GARDEN_BY_NAME_URL + gardenName);
  }


}

export default new Home();
