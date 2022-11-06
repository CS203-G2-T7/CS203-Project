import { Routes, Route, useLocation } from "react-router-dom";
import BallotGardenList from "pages/BallotGardenList/BallotGardenList";
import NotFound from "pages/404";
import Login from "pages/Login/Login";
import SignUp from "pages/SignUp/SignUp";
import Landing from "pages/Landing/Landing";
import MyPlants from "pages/MyPlants/MyPlants";
import AllGarden from "pages/AllGarden/AllGarden";
import BallotGarden from "pages/BallotGarden/BallotGarden";

function App() {
  const location = useLocation();
  // use the location to determine if the user is logged in
  return (
    <Routes location={location}>
      {/* Accessible to all */}
      <Route path="/" element={<Landing />} />
      <Route path="/landing" element={<Landing />} />
      <Route path="/ballot" element={<BallotGardenList />}/>
      <Route path="/ballot/:gardenName" element={<BallotGarden />} />
      <Route path="/login" element={<Login />} />
      <Route path="/sign-up" element={<SignUp />} />
      <Route path="/my-plants" element={<MyPlants />} />
      <Route path="/gardens" element={<AllGarden />} />

      {/* Accessible to NOT logged in users */}

      <Route path="*" element={<Login />} />
    </Routes>
  );
}

export default App;
