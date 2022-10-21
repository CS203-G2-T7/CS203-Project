package com.G2T7.OurGardenStory.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@DynamoDBTable(tableName = "User_Win_Garden_Ballot_JoinTable")
public class User {
    private String PK;
    private String SK;

    @DynamoDBAttribute
    private String FirstName;

    @DynamoDBAttribute
    private String LastName;

    @DynamoDBAttribute
    private String DOB;

    @DynamoDBAttribute
    private String Email;

    @DynamoDBAttribute
    private String Password;

    @DynamoDBAttribute
    private String Address;

    @DynamoDBAttribute
    private String PhoneNumber;

    @DynamoDBAttribute
    private String AccountDateCreated;

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
