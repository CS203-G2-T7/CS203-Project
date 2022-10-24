package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.repository.WindowRepo;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class WindowController {
    @Autowired
    private WindowRepo windowRepo;

    @GetMapping(path = "/windows")
    public List<Window> getWindows() {
        return windowRepo.listWindows();
    }

    @GetMapping(path = "/window")
    public Window getLatestWindow() {
        return windowRepo.findLatestWindow();
    }

    @PostMapping(path = "/window")
    public Window saveWindow(@RequestBody JsonNode payload) {
        Window window = new Window();
        window.setStartDateTime(LocalDateTime.parse(payload.get("startDateTime").asText()));
        window.setDuration(payload.get("duration").asText());
        String gardenName = payload.get("gardenName").asText();
        return windowRepo.save(window, gardenName);
    }

    @PutMapping(path = "/window")
    public Window updateWindow(@RequestBody JsonNode payload) {
        Window window = new Window();
        window.setStartDateTime(LocalDateTime.parse(payload.get("startDateTime").asText()));
        String gardenName = payload.get("gardenName").asText();
        return windowRepo.update(window, gardenName);
    }
}
