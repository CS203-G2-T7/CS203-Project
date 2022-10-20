// package com.G2T7.OurGardenStory.controller;

// import com.G2T7.OurGardenStory.model.User;
// import com.G2T7.OurGardenStory.repository.UserRepo;
// import com.G2T7.OurGardenStory.service.UserService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

// @CrossOrigin("*")
// @RestController
// public class UserController1 {
//     @Autowired
//     private UserService userService;

//     @GetMapping(path = "/user")
//     public List<User> findGardenById(@RequestParam(name = "id") Optional<String> id) {
//         // if (id.isPresent()) {
//         // List<Garden> result = new ArrayList<Garden>();
//         // result.add(gardenRepo.findGardenById(id.get()));
//         // return result;
//         // } else {
//         // return gardenRepo.listGardens();
//         // }
//         return new ArrayList<User>();
//     }

//     @PostMapping(path = "/user")
//     public User saveGarden(@RequestBody User user) {
//         return userService.createUser(user);
//     }
// }
