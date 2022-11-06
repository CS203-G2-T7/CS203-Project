import axios, { AxiosResponse } from "axios";

const PLACE_BALLOT_URL = "http://localhost:5000/window/win%w/ballot";
const GET_GARDEN_WIN_RELATION = "http://localhost:5000/window/win%w/garden?name=%n";

const config = {
  headers: {
    Authorization: `Bearer ${localStorage.getItem("jwtAccessToken")}`,
  },
};

class Ballot {
  placeBallot(gardenName: String, windowNum: number): Promise<AxiosResponse<any, any>> {
    return axios.post(
      PLACE_BALLOT_URL.replace("%w", windowNum.toString()),
      {
        gardenName: gardenName,
      },
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
        },
      }
    );
  }

  getGardenWinRelation(
    windowNum: number,
    gardenName: string
  ): Promise<AxiosResponse<any, any>> {
    return axios.get(
      GET_GARDEN_WIN_RELATION.replace("%w", windowNum.toString()).replace(
        "%n",
        gardenName.replace(" ", "-")
      )
    );
  }
}

export default new Ballot();
