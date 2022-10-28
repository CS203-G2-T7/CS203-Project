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
      <p>
        Allotment gardens are areas located within parks and gardens that house
        gardening plots available for lease to the community to grow their own
        plants. With most of the population living in high-rise flats with
        minimal space for gardening within their household footprint, these
        allotment garden plots provide them with further opportunities to
        garden.
      </p>
    </ContentStyled>
  );
}
