export type GardenWin = {
    ballotStatus: string;
    distance: number;
    numPlotsForBalloting: number;
    ballotId: string;
    sk: string;
    pk: string;
    winId_GardenName: string;
    ballotDateTime: string;
    leaseDuration: string;
  };
  
  export const defaultGardenWin: GardenWin = {
    ballotStatus: "",
    distance: 0.0,
    numPlotsForBalloting: 0,
    ballotId: "",
    sk: "",
    pk: "",
    winId_GardenName: "",
    ballotDateTime: "",
    leaseDuration: "",
  
  };
  