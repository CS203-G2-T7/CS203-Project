import React, { useEffect, useState } from "react";
import { CommunityStyled } from "./Community.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/Community/Header.styled";
import { ContentStyled } from "./Content/Content.styled";
import { Plant } from "models/Plant";
import Profile from "./Content/Profile";

export type User = {
  address: string;
  sk: string;
  email: string;
  dob: string;
  pk: string;
  plant: Plant[];
  phoneNumber: string;
  firstName: string;
  lastName: string;
  accountDateCreated: string;
};

export const defaultUser: User = {
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

const fakeDatabase: User[] = [
  {
    address: "38748",
    sk: "seanthan",
    email: "sean@sean.com",
    dob: "1994-10-10",
    pk: "User",
    plant: [],
    phoneNumber: "825289748",
    firstName: "sean",
    lastName: "sean",
    accountDateCreated: "2022-11-03",
  },
  {
    address: "234989",
    sk: "jefftan",
    email: "jeff@tam.com",
    dob: "1773-08-09",
    pk: "User",
    plant: [],
    phoneNumber: "3985394",
    firstName: "jeff",
    lastName: "tan",
    accountDateCreated: "2022-12-1",
  },
  {
    address: "2492834",
    sk: "bobs",
    email: "bobs@builder.com",
    dob: "1994-09-02",
    pk: "User",
    plant: [],
    phoneNumber: "29854",
    firstName: "bobs",
    lastName: "builder",
    accountDateCreated: "2022-06-03",
  },
];

export default function MyPlants() {
  // const [plantDataList, setPlantDataList] = useState<Plant[]>([defaultUser]);

  // useEffect(() => {
  //   Promise.all([plantService.getAllUserPlants()])
  //     .then((resArr) => {
  //       console.log(resArr[0].data);
  //       setPlantDataList(resArr[0].data);
  //     })
  //     .catch((err) => {
  //       console.log(err);
  //     });
  // }, []);

  return (
    <CommunityStyled>
      <NavBarStyled>
        <Navbar />
      </NavBarStyled>
      <HeaderStyled>
        <p>My Community</p>
        <p>View all the different profiles in your allotment garden</p>
      </HeaderStyled>
      <ContentStyled>
        {fakeDatabase.map((User, index) => (
          <Profile user={User} key={index} />
        ))}
      </ContentStyled>
    </CommunityStyled>
  );
}
