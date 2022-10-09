import React from "react";
import { CarouselLeftStyled } from "./CarouselLeft.styled";
import { Navigation, Pagination, Scrollbar, A11y } from "swiper";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";

type Props = {};

export default function Carousel({}: Props) {
  return (
    <CarouselLeftStyled>
      <Swiper
        spaceBetween={50}
        slidesPerView={1}
        modules={[Navigation, Pagination]}
        navigation
        pagination={{ clickable: true }}
      >
        <SwiperSlide>
          <img src={require("assets/imgs/AMKGarden.jpg")} alt="slide1" />
        </SwiperSlide>
        <SwiperSlide>
          <img src={require("assets/imgs/MapDirections.jpg")} alt="slide2" />
        </SwiperSlide>
      </Swiper>
    </CarouselLeftStyled>
  );
}
