import React from "react";
import { CarouselStyled } from "./Carousel.styled";
import { Navigation, Pagination, Scrollbar, A11y } from "swiper";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";

type Props = {};

export default function Carousel({}: Props) {
  return (
    <CarouselStyled>
      <Swiper
        spaceBetween={50}
        slidesPerView={1}
        modules={[Navigation, Pagination]}
        navigation
        pagination={{ clickable: true }}
      >
        <SwiperSlide>
          <img src={require("assets/imgs/PunggolParkLanding.jpg")} alt="slide1" />
        </SwiperSlide>
        <SwiperSlide>
          <img src={require("assets/imgs/ECPLanding.jpg")} alt="slide2" />
        </SwiperSlide>
        <SwiperSlide>
          <img src={require("assets/imgs/HortParkLanding.png")} alt="slide3" />
        </SwiperSlide>
        <SwiperSlide>
          <img src={require("assets/imgs/BishanAMKLanding.jpg")} alt="slide4" />
        </SwiperSlide>
      </Swiper>
    </CarouselStyled>
  );
}