package com.G2T7.OurGardenStory.model.RelationshipModel;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Ballot extends Relationship {
    public enum BallotStatus {
        PENDING("PENDING"),
        SUCCESS("SUCCESS"),
        FAIL("FAIL"),
        INVALID("INVALID");

        public final String value;

        BallotStatus(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum PaymentStatus {
        PENDING("PENDING"),
        SUCCESS("SUCCESS"),
        FAILED("FAILED");

        public final String value;

        PaymentStatus(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @DynamoDBIgnore
    public static int numInstance;

    public Ballot(String winId, String username, String gardenName, String ballotId, String ballotDateTime,
            double distance, int numPlotsForBalloting, String ballotStatus, String paymentStatus) {
        super(winId, username, winId + "_" + gardenName, "3Y", numPlotsForBalloting, ballotId, ballotDateTime, distance,
                ballotStatus, paymentStatus);
    }

}
