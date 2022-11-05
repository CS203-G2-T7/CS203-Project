export type Garden = {
  gardenAddress: string;
  latitude: string;
  longitude: string;
  numPlots: number;
  pk: string;
  sk: string;
};

export const defaultGarden: Garden = {
  gardenAddress: "",
  latitude: "",
  longitude: "",
  numPlots: 0,
  pk: "",
  sk: "",
};
