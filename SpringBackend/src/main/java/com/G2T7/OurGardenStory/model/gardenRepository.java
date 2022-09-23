package com.G2T7.OurGardenStory.model;

import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;


public class gardenRepository {
     
    private int gardenId;

    @NotNull(message = "Location should not be null")
    private String location;

    @NotNull(message = "Number of plots should not be null")
    private int numPlots;

    public int getGardenId() {
        return gardenId;
    }

    public void setGardenId(int gardenId) {
        this.gardenId = gardenId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumPlots() {
        return numPlots;
    }

    public void setNumPlots(int numPlots) {
        this.numPlots = numPlots;
    }

    


}
