import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { BrowserRouter } from "react-router-dom";
import { GlobalStyles, muiTheme } from "config/GlobalStyles";
import { ThemeProvider } from "@mui/material";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

root.render(
    <BrowserRouter>
      <GlobalStyles />
      <ThemeProvider theme={muiTheme}>
        <App />
      </ThemeProvider>
    </BrowserRouter>
);
