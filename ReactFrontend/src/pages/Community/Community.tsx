import React, { useEffect, useState } from "react";
import { CommunityStyled, ParkNameStyled } from "./Community.styled";
import Navbar from "components/Navbar/Navbar";
import { NavBarStyled } from "pages/Landing/NavBar.styled";
import { HeaderStyled } from "pages/Community/Header.styled";
import { ContentStyled } from "./Content/Content.styled";
import Profile from "./Content/Profile";
import communityService from "service/communityService";

export type User = {
  username: string;
  email: string;
  gardenName: string;
};

export const defaultUser: User = {
  username: "",
  email: "",
  gardenName: "",
};

export default function MyPlants() {
  const [userDataList, setUserDataList] = useState<User[]>([defaultUser]);
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
        {userDataList.length === 0
          ? "Please ballot for a garden to join a community"
          : "View all the different profiles in your allotment garden community at " +
          userDataList[0].gardenName}
      </HeaderStyled>
      <ParkNameStyled>
        {userDataList.length === 0
          ? "Join a community by balloting for an allotment garden!"
          : "My " + userDataList[0].gardenName + " Community"}
      </ParkNameStyled>
      <ContentStyled>
        {userDataList.map((User, index) => (
          <Profile user={User} key={index} />
        ))}
      </ContentStyled>
    </CommunityStyled>
  );
}
