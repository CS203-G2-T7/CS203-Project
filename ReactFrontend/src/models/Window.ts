import { defaultGarden, Garden } from "./Garden";

export type Window = {
  // windowId: string; //PKey
  // startDateTime: string;
  // duration: string;
  // leaseStart: string;
  // gardenList: Garden[]; //FKey embed: Garden
  windowId: string;
  sk: string;
  windowDuration: string;
  pk: string;

};

export const defaultWindow: Window = {
  // windowId: "",
  // startDateTime: "",
  // duration: "",
  // leaseStart: "",
  // gardenList: [defaultGarden],
  windowId: "",
  sk: "",
  windowDuration: "",
  pk: "",
};
