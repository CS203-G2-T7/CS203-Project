package com.G2T7.OurGardenStory.model;

import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "Ballot")

public class Ballot {
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String ballotId;

    @DynamoDBAttribute
    private LocalDateTime submitDateTime;

    @DynamoDBAttribute
    private LocalDateTime startDateTime;

    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private String leaseDuration = "3Y";

//    private enum status{
//        FAIL,PENDING,SUCCESS,INVALID;
//    }

    @DynamoDBTypeConverted( converter = LocalDateTimeConverter.class )
    public LocalDateTime getSubmitDateTime() { return submitDateTime;}

    @DynamoDBTypeConverted( converter = LocalDateTimeConverter.class )
    public LocalDateTime getStartDateTime() { return startDateTime;}

    static public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

        @Override
        public String convert( final LocalDateTime time ) {
            return time.toString();
        }
        @Override
        public LocalDateTime unconvert( final String stringValue ) {
            return LocalDateTime.parse(stringValue);
        }
    }
}
