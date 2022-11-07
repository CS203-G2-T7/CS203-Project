import React, { useEffect } from "react";
import styled from "styled-components";
import { SpringValue, animated } from "react-spring";

type Props = {
  onCloseModal: React.Dispatch<React.SetStateAction<boolean>> | (() => void);
  children?: React.ReactNode;
  style: {
    opacity: SpringValue<number>;
  };
};

export const BackDropStyle = styled(animated.div)`
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  height: 100%;
  z-index: 10;
  background-color: rgba(0, 0, 0, 0.75);
`;

function stopScroll(): void {
  const body = document.getElementsByTagName("body")[0];
  body.classList.add("stopBodyScroll"); //add stopScroll class whenever overlay is mounted
}

function startScroll(): void {
  const body = document.getElementsByTagName("body")[0];
  body.classList.remove("stopBodyScroll"); //remove stopScroll class whenever overlay is dismounted
}

export default function Overlay({ children, onCloseModal, style }: Props) {
  useEffect(() => {
    stopScroll();
  }, []);
  useEffect(() => () => startScroll(), []);

  return (
    <BackDropStyle style={style} onClick={() => onCloseModal(false)}>
      {children}
    </BackDropStyle>
  );
}
