import axios, { AxiosResponse } from "axios";

const LATEST_WINDOW_URL = "http://localhost:5000/window";
const BALLOTS_LATEST_WINDOW_URL = "http://localhost:5000/ballot";

class Home {
  //gets Window, which contains array of Gardens
  getLatestWindow(): Promise<AxiosResponse<any, any>> {
    return axios.get(LATEST_WINDOW_URL);
  }
  getBallotsLatestWindow(): Promise<AxiosResponse<any, any>> {
    return axios.get(BALLOTS_LATEST_WINDOW_URL);
  }
}

export default new Home();
