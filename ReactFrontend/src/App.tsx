import { Routes, Route, useLocation } from "react-router-dom";
import BallotGardenList from "pages/BallotGardenList/BallotGardenList";
import Login from "pages/Login/Login";
import SignUp from "pages/SignUp/SignUp";
import Landing from "pages/Landing/Landing";
import MyPlants from "pages/MyPlants/MyPlants";
import AllGarden from "pages/AllGarden/AllGarden";
import BallotGarden from "pages/BallotGarden/BallotGarden";
import Garden from "pages/Garden/Garden";

function App() {
  const location = useLocation();
  // use the location to determine if the user is logged in
  return (
    <Routes location={location}>
      {/* Accessible to all */}
      <Route path="/" element={<Landing />} />
      <Route path="/ballot" element={<BallotGardenList />} />
      <Route path="/ballot/:gardenName" element={<BallotGarden />} />
      <Route path="/login" element={<Login />} />
      <Route path="/sign-up" element={<SignUp />} />
      <Route path="/my-plant" element={<MyPlants />} />
      <Route path="/garden" element={<AllGarden />} />
      <Route path="/garden/:gardenName" element={<Garden />} />

      {/* Accessible to NOT logged in users */}

      <Route path="*" element={<Login />} />
    </Routes>
  );
}

export default App;
