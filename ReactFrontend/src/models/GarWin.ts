export const defaultGarWin: GarWin = {
  ballotStatus: "",
  distance: 0,
  numPlotsForBalloting: 0,
  sk: "",
  ballotId: "",
  pk: "",
  winId_GardenName: "",
  ballotDateTime: "",
  leaseDuration: "",
};

export type GarWin = {
  ballotStatus: string;
  distance: number;
  numPlotsForBalloting: number;
  sk: string;
  pk: string;
  ballotId: string;
  winId_GardenName: string;
  ballotDateTime: string;
  leaseDuration: string;
};
