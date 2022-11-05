export type Ballot = {
  leaseDuration: string;
  numPlotsForBalloting: number;
  ballotId: string;
  ballotDateTime: string;
  distance: number;
  ballotStatus: String;
  winId_GardenName : String;
  pk: string;
  sk: string;
};

export const defaultBallot = {
  leaseDuration: "",
  NumPlotsForBalloting: 0,
  BallotId: "",
  BallotDateTime: "",
  Distance: 0,
  ballotStatus: "",
  winId_GardenName : "",
  pk: "",
  sk: "",
};
