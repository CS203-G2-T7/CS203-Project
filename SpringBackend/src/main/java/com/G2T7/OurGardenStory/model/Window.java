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
    public static final String entityName = "Window";

    private String PK = "Window";
    private String SK;
    private String windowId;
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

    public void setSK(String SK) {
        this.SK = SK;
    }

    @DynamoDBIndexHashKey(attributeName = "WindowId", globalSecondaryIndexName = "WindowId-index")
    public String getWindowId() {
        return this.windowId;
    }

    public void setWindowId(String windowId) {
        this.windowId = windowId;
    }
}
