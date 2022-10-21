package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.repository.WindowRepo;
import com.G2T7.OurGardenStory.service.WindowService;
import com.amazonaws.services.cognitoidp.model.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController

public class WindowController {
    @Autowired
    private WindowService windowService;

    @Autowired
    private WindowRepo windowRepo;

    @GetMapping(path = "/window")
    public List<Window> findWindowByID(@RequestParam(name = "id") Optional<String> id) {
        return new ArrayList<Window>();
    }

    @PostMapping(path = "/window")
    public Window saveGarden(@RequestBody Window window) {
        return windowService.createWindow(window);
    }

    @PutMapping(path = "/window")
    public void updateWindow(@PathVariable String WindowID, @RequestBody Window window, @RequestBody String garden) {
        Window updateWindow = windowRepo.findById(WindowID)
                .orElseThrow(() -> new ResourceNotFoundException("Window does not exist with id: " + WindowID));
        
        updateWindow.setSK(garden);

        windowRepo.save(updateWindow);       
    }
}
