export const defaultPlant: Plant = {
    address: "",
    sk: "",
    email: "",
    dob: "",
    pk: "",
    plant: [],
    phoneNumber: "",
    firstName: "",
    lastName: "",
    accountDateCreated: "",
  };
  
  export type Plant = {
    address: string;
    sk: string;
    email: string;
    dob: string;
    pk: string
    plant: Plant[],
    phoneNumber: string;
    firstName: string;
    lastName: string;
    accountDateCreated: string;
  };
  