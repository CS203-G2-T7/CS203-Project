import axios, { AxiosResponse } from "axios";

<<<<<<< HEAD
const LATEST_WINDOW_URL = "http://localhost:5000/window/latest";
const LATEST_WINDOW_GARDEN_URL = "http://localhost:5000/window/win%n/garden";
const GET_GARDEN_BY_NAME_URL = "http://localhost:5000/garden?name="; //get address
const LATEST_WINDOW_GARDEN_ALLBALLOTS_URL =
  "http://localhost:5000/window/win%n/%s/allBallot"; //Get arr of ballots. Count size of array.
=======
const LATEST_WINDOW_GARDEN_URL = "http://localhost:5000/window/win1/garden";//hardcoded
const GET_WINDOW_BY_ID_URL  = "http://localhost:5000/window?id=win1";
const LATEST_WINDOW_GARDEN_ALLBALLOTS_URL = "http://localhost:5000/window/win1/allBallot";
const GET_GARDEN_BY_NAME_URL = "http://localhost:5000/garden?name=";
>>>>>>> a06213bddebd8c1efdc46920fd8d0a97abd7efda

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

<<<<<<< HEAD
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
=======

>>>>>>> a06213bddebd8c1efdc46920fd8d0a97abd7efda
}

export default new Home();
