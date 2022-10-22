package com.G2T7.OurGardenStory.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@DynamoDBTable(tableName = "User_Win_Garden_Ballot_JoinTable")

public class Window {
    private String PK;
    private String SK;

    @DynamoDBAttribute
    private String WindowID;

    @DynamoDBAttribute
    private String WindowDuration;

    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        return this.PK;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    @DynamoDBRangeKey(attributeName = "SK")
    public String getSK() {
        return SK;
    }

    public void setSK(String sK) {
        SK = sK;
    }
}
