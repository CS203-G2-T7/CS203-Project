package com.G2T7.OurGardenStory.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Plants")
public class Plant {
    public static final String EntityName = "Plant";
    private String PK;
    private String SK;
    
    // @DynamoDBAttribute
    // private String plantId;

    @DynamoDBAttribute
    private String plantSpecies;
    @DynamoDBAttribute
    private String description;
    // @DynamoDBAttribute
    // private String plantCare;
    // @DynamoDBAttribute
    // private String waterAmount;

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

    // @DynamoDBAttribute
    // private fruitSeed fruitSeedAbundance;

    // @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    // private LocalDateTime registrationDateTime;

}

// enum fruitSeed {
//     HIGH,LOW,MEDIUM,NONE;
// }
