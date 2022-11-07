import React, { useEffect, useState } from "react";
import { CommunityStyled } from "./Community.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/Community/Header.styled";
import { ContentStyled } from "./Content/Content.styled";
import { Plant } from "models/Plant";
import Profile from "./Content/Profile";
import communityService from "service/communityService";

// export type User = {
//   userName:string;
//   email:string;
// };

// export const defaultUser: User = {
//   userName:"",
//   email:"",
// };

// export type Community = {
//   users: User[];
//   gardenName: string;
// };

// export const defaultCommunity: Community = {
//   users:[],
//   gardenName:"",
// };

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

export default function MyPlants() {
  const [userDataList, setUserDataList] = useState<User[]>([
    defaultUser,
  ]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    communityService
      .getAllUsersByGarden()
      .then((res) => {
        console.log(res.data);
        setUserDataList(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);


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
        {userDataList.map((User, index) => (
          <Profile user={User} key={index} />
        ))}
      </ContentStyled>
    </CommunityStyled>
  );
}
