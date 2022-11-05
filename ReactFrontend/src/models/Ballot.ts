import internal from "stream";
import { defaultGarden, Garden } from "./Garden";

export type Ballot = {
  // ballotId: string; //PKey
  // submitDateTime: string;
  // leaseStart: string;
  // leaseDuration: string;
  // status: string;
  // garden: Garden; //FKey embed: Garden
  // startDateTime: string; //FKey: windowId
  // username: string;
  leaseDuration: string;
  NumPlotsForBalloting: number;
  BallotId: string;
  BallotDateTime: string;
  Distance: number;
  ballotStatus: String;
  winId_GardenName : String;
  pk: string;
  sk: string;



};

export const defaultBallot = {
  // ballotId: "",
  // submitDateTime: "",
  // leaseStart: "",
  // leaseDuration: "",
  // status: "",
  // garden: defaultGarden,
  // startDateTime: "",
  // username: "",
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
