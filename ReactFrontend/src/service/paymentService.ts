import axios, { AxiosResponse } from "axios";
import { prod_url } from "urlConstants";

const PAYMENT_URL = `${prod_url}/payment`;

class Payment {
  getPaymentStatus(): Promise<AxiosResponse<any, any>> {
    return axios.get(PAYMENT_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }

  sendPayment(): Promise<AxiosResponse<any, any>> {
    return axios.post(PAYMENT_URL, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwtAccessToken"),
      },
    });
  }
}

export default new Payment();
