import React from "react";
import { ContentStyled } from "./Content.styled";
import { Navigation, Pagination, Scrollbar, A11y } from "swiper";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import Carousel from "./Carousel/Carousel";

type Props = {};

export default function Content({}: Props) {
  return (
    <ContentStyled>
        <Carousel />
    </ContentStyled>
  );
}