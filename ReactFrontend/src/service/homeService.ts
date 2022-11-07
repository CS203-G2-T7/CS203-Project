import axios, { AxiosResponse } from "axios";
import { prod_url } from "urlConstants";

const LATEST_WINDOW_URL = `${prod_url}/window/latest`;
const LATEST_WINDOW_GARDEN_URL = `${prod_url}/window/win%n/garden`;
const GET_GARDEN_BY_NAME_URL = `${prod_url}/garden?name=`; //get address
const LATEST_WINDOW_GARDEN_ALLBALLOTS_URL = `${prod_url}/window/win%n/%s/allBallot`; //Get arr of ballots. Count size of array.

class Home {
  getLatestWindow(): Promise<AxiosResponse<any, any>> {
    return axios.get(LATEST_WINDOW_URL);
  }

  getGardenInLatestWindow(
    latestWindow: number
  ): Promise<AxiosResponse<any, any>> {
    return axios.get(
      LATEST_WINDOW_GARDEN_URL.replace("%n", latestWindow.toString()),
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
        },
      }
    );
  }

  getGardenByName(gardenName: String): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_GARDEN_BY_NAME_URL + gardenName, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }

  getAllBallotsInWindGarden(
    latestWindow: number,
    gardenName: string
  ): Promise<AxiosResponse<any, any>> {
    return axios.get(
      LATEST_WINDOW_GARDEN_ALLBALLOTS_URL.replace(
        "%n",
        latestWindow.toString()
      ).replace("%s", gardenName),
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
        },
      }
    );
  }
}

export default new Home();
