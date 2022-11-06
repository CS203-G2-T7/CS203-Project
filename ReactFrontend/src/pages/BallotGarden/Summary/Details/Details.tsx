import React, { useEffect, useState } from "react";
import ballotService from "service/ballotService";
import { DetailsGroupStyled } from "./DetailsGroup.styled";
import { DetailsRightStyled } from "./DetailsRight.styled";
import { SummaryButtonStyled } from "./SummaryButton.styled";
import { Alert, IconButton, Snackbar } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { DetailsType } from "pages/BallotGarden/BallotGarden";
import { DetailsRowStyled } from "./DetailRow.styled";

type Props = {
  details: DetailsType;
  gardenName: string;
  winNum: number;
};

export default function Details({ details, gardenName, winNum }: Props) {
  const [open, setOpen] = useState(false);
  const [numBallot, setNumBallot] = useState(details.ballotsPlaced);

  useEffect(() => {
    setNumBallot(details.ballotsPlaced);
  }, [details.ballotsPlaced]);


  console.log(details);

  //Place ballot
  const submitBallotHandler = () => {
    console.log("Submitted");
    setOpen(true);
    setNumBallot((prevNumBallot) => {
      if (prevNumBallot === details.ballotsPlaced + 1) {
        return prevNumBallot;
      }
      return ++prevNumBallot;
    });
    ballotService
      .placeBallot(gardenName, winNum)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const action = (
    <>
      <IconButton
        size="small"
        aria-label="close"
        color="inherit"
        onClick={() => {
          setOpen(false);
        }}
      >
        <CloseIcon fontSize="small" />
      </IconButton>
    </>
  );

  return (
    <DetailsRightStyled>
      <DetailsGroupStyled>
        <DetailsRowStyled>
          <h3>Garden Details</h3>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Total number of plots</p>
          <p>{details.numPlots}</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Lease date</p>
          <p>{details.leaseDate}</p>
        </DetailsRowStyled>
      </DetailsGroupStyled>
      <DetailsGroupStyled>
        <DetailsRowStyled>
          <h3>Ballot Details</h3>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Window</p>
          <p>{details.window}</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Available plots</p>
          <p>{details.availablePlots}</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Ballots Placed</p>
          <p>{numBallot}</p>
        </DetailsRowStyled>
      </DetailsGroupStyled>
      <SummaryButtonStyled
        variant="contained"
        onClick={() => submitBallotHandler()}
        disabled={numBallot > details.ballotsPlaced}
      >
        Place Ballot
      </SummaryButtonStyled>
      <Snackbar
        open={open}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
        autoHideDuration={6000}
        onClose={() => {
          setOpen(false);
        }}
        action={action}
      >
        <Alert
          onClose={() => {
            setOpen(false);
          }}
          severity="success"
          sx={{ width: "100%" }}
        >
          Ballot Placed
        </Alert>
      </Snackbar>
    </DetailsRightStyled>
  );
}
