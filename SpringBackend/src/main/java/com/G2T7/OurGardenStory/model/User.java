package com.G2T7.OurGardenStory.model;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@DynamoDBTable(tableName = "User_Win_Garden_Ballot_JoinTable")
public class User {
    public static final String EntityName = "User";
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
    private String Address;

    @DynamoDBAttribute
    private String PhoneNumber;

    @DynamoDBAttribute
    private String AccountDateCreated;

    @DynamoDBAttribute
    private List<String> Plant;

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

    // static public class LocalDateTimeConverter implements
    // DynamoDBTypeConverter<String, LocalDateTime> {

    // @Override
    // public String convert(final LocalDateTime time) {
    // return time.toString();
    // }

    // @Override
    // public LocalDateTime unconvert(final String stringValue) {
    // return LocalDateTime.parse(stringValue);
    // }
    // }
}
