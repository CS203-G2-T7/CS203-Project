import Navbar from "components/Navbar/Navbar";
import React, { useEffect, useState } from "react";
import paymentService from "service/paymentService";
import Header from "./Header";
import { PaymentStyled } from "./Payment.styled";

export default function Payment() {
  const [paymentStatus, setPaymentStatus] = useState("");
  const [gardenName, setGardenName] = useState("");
  useEffect(() => {
    paymentService
      .getPaymentStatus()
      .then((res) => {
        console.log(res);
        setPaymentStatus(
          "Payment for " +
            res.data.description.replace("_", ", ") +
            " required."
        );
        setGardenName(res.data.description.split("_")[1]);
      })
      .catch((e) => {
        console.log(e);
        setPaymentStatus("No payment required");
      });
  }, []);

  return (
    <PaymentStyled>
      <Header garden={gardenName} />
    </PaymentStyled>
  );
}
