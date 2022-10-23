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
public class Window {
    @DynamoDBIgnore
    public static int numInstance;

    private String PK = "Window"; // Window objects have a default PK of "Window"
    private String SK;

    @DynamoDBIndexHashKey(attributeName = "WindowId")
    private String WindowId;

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
