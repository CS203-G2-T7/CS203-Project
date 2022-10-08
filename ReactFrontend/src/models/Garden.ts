export type Garden = {
  gardenId: string; //PKey
  latitude: string;
  location: string;
  longitude: string;
  name: string;
  numPlots: number;
};

export const defaultGarden: Garden = {
  gardenId: "",
  latitude: "",
  location: "",
  longitude: "",
  name: "",
  numPlots: 0,
};
