import { Routes, Route, useLocation } from "react-router-dom";
import Home from "pages/Home/Home";
import NotFound from "pages/404";
import Login from "pages/Login/Login";
import SignUp from "pages/SignUp/SignUp";
import Garden from "pages/Garden/Garden";

function App() {
  const location = useLocation();
  // use the location to determine if the user is logged in
  return (
    <Routes location={location}>
      {/* Accessible to all */}
      <Route path="/" element={<Login />} />
      <Route path="/home" element={<Home />} />
      <Route path="/login" element={<Login />} />
      <Route path="/sign-up" element={<SignUp />} />
      <Route path="/garden" element={<Garden />} />

      {/* Accessible to NOT logged in users */}

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

export default App;
