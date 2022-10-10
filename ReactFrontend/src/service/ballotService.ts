import axios, { AxiosResponse } from "axios";

const PLACE_BALLOT_URL = "http://localhost:5000/ballot";
const CHOOSE_BALLOT_URL = "http://localhost:5000/magic";

const config = {
  headers: {
    Authorization: `Bearer ${localStorage.getItem("jwtAccessToken")}`,
  },
};

class Ballot {
  placeBallot(gardenName: String): Promise<AxiosResponse<any, any>> {
    // console.log(config.headers.Authorization);
    return axios.post(
      PLACE_BALLOT_URL,
      {
        gardenName: gardenName,
      },
      {
        headers: {
          Authorization: "Bearer " + localStorage.getItem('jwtAccessToken'),
        },
      }
    );
  }
  chooseBallot(): Promise<AxiosResponse<any, any>> {
    return axios.get(CHOOSE_BALLOT_URL);
  }
}

export default new Ballot();
