import React, { useEffect, useState } from "react";
import Table from "./Table/Table";
import { ContentStyled } from "./Content.styled";
import WindowLabel from "./WindowLabel/WindowLabel";
import homeService from "service/homeService";
import { AxiosResponse } from "axios";
import formatDateTimeToDate from "utils/formatDateTimeToDate";

type Props = {};

export type Garden = {
  gardenId: string;
  latitude: string;
  location: string;
  longitude: string;
  name: string;
  numPlots: number;
};

export default function Content({}: Props) {
  //get data from backend
  let [homeData, setHomeData] = useState<AxiosResponse<any, any>>();
  useEffect(() => {
    homeService
      .getHomeData()
      .then((res) => {
        console.log(res.data);
        setHomeData(res);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  //get formatted window date
  const windowDate: string =
    formatDateTimeToDate(homeData?.data.startDateTime ?? "") +
    " - " +
    formatDateTimeToDate(homeData?.data.leaseStart ?? "");

  const gardenList: Garden[] = homeData?.data.gardenList ?? [];
  console.log(gardenList);

  return (
    <ContentStyled>
      <WindowLabel windowDate={windowDate} />
      <Table gardenList={gardenList}/>
    </ContentStyled>
  );
}
