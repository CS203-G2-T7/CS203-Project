export type Garden = {
  // gardenId: string; //PKey
  // latitude: string;
  // location: string;
  // longitude: string;
  // name: string;
  // numPlots: number;
  gardenAddress: string;
  latitude: string;
  longitude: string;
  numPlots: number;
  pk: string;
  sk: string;
};

export const defaultGarden: Garden = {
  // gardenId: "",
  // latitude: "",
  // location: "",
  // longitude: "",
  // name: "",
  // numPlots: 0,
  gardenAddress: "",
  latitude: "",
  longitude: "",
  numPlots: 0,
  pk: "",
  sk: "",

};
