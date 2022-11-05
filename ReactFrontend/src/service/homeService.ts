import axios, { AxiosResponse } from "axios";

const LATEST_WINDOW_URL = "http://localhost:5000/window/latest";
const LATEST_WINDOW_GARDEN_URL = "http://localhost:5000/window/win%n/garden";
const GET_GARDEN_BY_NAME_URL = "http://localhost:5000/garden?name="; //get address
const LATEST_WINDOW_GARDEN_ALLBALLOTS_URL =
  "http://localhost:5000/window/win%n/%s/allBallot"; //Get arr of ballots. Count size of array.

class Home {
  getLatestWindow(): Promise<AxiosResponse<any, any>> {
    return axios.get(LATEST_WINDOW_URL);
  }

  getGardenInLatestWindow(
    latestWindow: number
  ): Promise<AxiosResponse<any, any>> {
    return axios.get(
      LATEST_WINDOW_GARDEN_URL.replace("%n", latestWindow.toString())
    );
  }

  getGardenByName(gardenName: String): Promise<AxiosResponse<any, any>> {
    return axios.get(GET_GARDEN_BY_NAME_URL + gardenName);
  }

  getAllBallotsInWindGarden(
    latestWindow: number,
    gardenName: string
  ): Promise<AxiosResponse<any, any>> {
    console.log(
      LATEST_WINDOW_GARDEN_ALLBALLOTS_URL.replace(
        "%n",
        latestWindow.toString()
      ).replace("%s", gardenName)
    );
    return axios.get(
      LATEST_WINDOW_GARDEN_ALLBALLOTS_URL.replace(
        "%n",
        latestWindow.toString()
      ).replace("%s", gardenName)
    );
  }
}

export default new Home();
