import { Button } from "@mui/material";
import React, { useEffect, useState } from "react";
import paymentService from "service/paymentService";
import Header from "./Header";
import { PaymentStyled } from "./Payment.styled";
import Row from "./Row";
import { StyledButton } from "./Button.styled";
import { AxiosResponse } from "axios";

type paymentRes = {
  description: string;
  amount: number;
  currency: string;
};

const defaultPaymentRes: paymentRes = {
  description: "",
  amount: 0,
  currency: "",
};

export default function Payment() {
  const [paymentStatus, setPaymentStatus] =
    useState<paymentRes>(defaultPaymentRes);
  const [paymentComplete, setPaymentComplete] = useState(false);
  let successMessage: string = "";
  useEffect(() => {
    paymentService
      .getPaymentStatus()
      .then((res: AxiosResponse<any, any>) => {
        setPaymentStatus(res.data);
      })
      .catch((e: Error) => {
        console.log(e);
        setPaymentStatus({
          description: "_No payment required",
          amount: 0,
          currency: "",
        });
      });
  }, []);

  const checkoutHandler = (
    event: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    //call payment service
    //success -> disable button
    //fail -> whatever

    paymentService
      .sendPayment()
      .then((res: AxiosResponse<any, any>) => {
        console.log(res.data);
        successMessage = res.data;
        setPaymentComplete(true);
      })
      .catch((e: Error) => {
        console.log(e);
      });
  };

  const gardenName: string = paymentStatus.description.split("_")[1];
  const windowId: string = paymentStatus.description.split("_")[0];

  return (
    <PaymentStyled>
      <Header garden={gardenName} />

      {paymentComplete ? (
        <>
          <div>Payment successful</div>
          <p>{successMessage}</p>
        </>
      ) : (
        <Row
          gardenName={gardenName}
          windowId={windowId}
          paymentAmount={paymentStatus.amount}
        />
      )}
      <StyledButton>
        <Button
          variant="contained"
          onClick={(e) => {
            checkoutHandler(e);
          }}
          disabled={paymentComplete || paymentStatus.amount === 0}
        >
          Checkout
        </Button>
      </StyledButton>
    </PaymentStyled>
  );
}
