package com.G2T7.OurGardenStory.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "User_Win_Garden_Ballot_JoinTable")
public class Garden {
    @DynamoDBAttribute(attributeName = "PK") // Should I do this? Or should I just name the attribute PK?
    private String GardenName; // not needed. Identified by PK

    @DynamoDBAttribute
    private String SK;

    @DynamoDBAttribute
    private String Address;

    @DynamoDBAttribute
    private int NumPlots;

    @DynamoDBAttribute
    private String Latitude;

    @DynamoDBAttribute
    private String Longitude;
}
