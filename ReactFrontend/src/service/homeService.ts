import axios from "axios";

const HOME_BASE_URL = "http://localhost:5000/window";

class Home {
  getHomeData() {
    return axios.get(HOME_BASE_URL);
  }
}

export default new Home();
