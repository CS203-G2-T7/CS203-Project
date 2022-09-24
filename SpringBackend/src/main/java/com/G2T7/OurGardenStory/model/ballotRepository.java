package com.G2T7.OurGardenStory.model;

import java.time.*;

public class ballotRepository {
    private String ballotId;

    private LocalDate submitDateTime;

    private enum status{
        FAIL,PENDING,SUCCESS,INVALID;
    }

    public String getBallotId() {
        return ballotId;
    }

    public void setBallotId(String ballotId) {
        this.ballotId = ballotId;
    }

    public LocalDate getSubmitDateTime() {
        return submitDateTime;
    }

    public void setSubmitDateTime(LocalDate submitDateTime) {
        this.submitDateTime = submitDateTime;
    }

    
}
