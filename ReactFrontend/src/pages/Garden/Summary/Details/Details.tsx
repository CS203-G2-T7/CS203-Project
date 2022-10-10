import React from "react";
import { LinkStateType } from "pages/Garden/Garden";
import ballotService from "service/ballotService";
import formatDateTimeToDate from "utils/formatDateTimeToDate";
import { DetailsRowStyled } from "./DetailRow.styled";
import { DetailsGroupStyled } from "./DetailsGroup.styled";
import { DetailsRightStyled } from "./DetailsRight.styled";
import { SummaryButtonStyled } from "./SummaryButton.styled";

type Props = {
  linkState: LinkStateType;
};

export default function Details({ linkState }: Props) {
  //Format data to display
  let leaseStartDate: string = formatDateTimeToDate(
    linkState.windowData.leaseStart
  );
  const leaseEndYear = Number(leaseStartDate.substring(6)) + 3 + "";
  leaseStartDate =
    leaseStartDate + " - " + leaseStartDate.slice(0, 6) + leaseEndYear;

  const windowDate: string =
    formatDateTimeToDate(linkState.windowData.startDateTime ?? "") +
    " - " +
    formatDateTimeToDate(linkState.windowData.leaseStart ?? "");

  const ballotsPlaced = linkState.numBallotsPlaced;

  //Place ballot
  const submitBallotHandler = () => {
    console.log("Submitted");
    ballotService
      .placeBallot(linkState.gardenObject.name)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  return (
    <DetailsRightStyled>
      <DetailsGroupStyled>
        <DetailsRowStyled>
          <h3>Garden Details</h3>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Number of plots</p>
          <p>{linkState.gardenObject.numPlots}</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Available plots</p>
          <p>1</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Lease date</p>
          <p>{leaseStartDate}</p>
        </DetailsRowStyled>
      </DetailsGroupStyled>
      <DetailsGroupStyled>
        <DetailsRowStyled>
          <h3>Ballot Details</h3>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Window</p>
          <p>{windowDate}</p>
        </DetailsRowStyled>
        <DetailsRowStyled>
          <p>Ballots Placed</p>
          <p>{ballotsPlaced}</p>
        </DetailsRowStyled>
      </DetailsGroupStyled>
      <SummaryButtonStyled
        variant="contained"
        onClick={() => submitBallotHandler()}
      >
        Place Ballot
      </SummaryButtonStyled>
    </DetailsRightStyled>
  );
}
