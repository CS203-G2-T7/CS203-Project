package com.G2T7.OurGardenStory.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@DynamoDBTable(tableName = "User_Win_Garden_Ballot_JoinTable")
public class Garden {
    private String PK;
    private String SK;

    @DynamoDBAttribute
    private String GardenAddress;
    @DynamoDBAttribute
    private String NumPlots;
    @DynamoDBAttribute
    private String Latitude;
    @DynamoDBAttribute
    private String Longitude;

    // Strangely for PK and SK, can't use Lombok annotation.
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
