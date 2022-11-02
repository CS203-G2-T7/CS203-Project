package com.G2T7.OurGardenStory.model.RelationshipModel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Ballot extends Relationship {
    @DynamoDBIgnore
    public static int numInstance;

    public Ballot(String winId, String username, String gardenName, String ballotId, String ballotDateTime,
            double distance, String ballotStatus) {
        super(winId, username, winId + "|" + gardenName, null, 0, ballotId, ballotDateTime, distance,
                ballotStatus);
    }
}



