import { defaultGarden, Garden } from "./Garden";

export type Ballot = {
  ballotId: string; //PKey
  submitDateTime: string;
  leaseStart: string;
  leaseDuration: string;
  status: string;
  garden: Garden; //FKey embed: Garden
  startDateTime: string; //FKey: windowId
  username: string;
};

export const defaultBallot = {
  ballotId: "",
  submitDateTime: "",
  leaseStart: "",
  leaseDuration: "",
  status: "",
  garden: defaultGarden,
  startDateTime: "",
  username: "",
};
