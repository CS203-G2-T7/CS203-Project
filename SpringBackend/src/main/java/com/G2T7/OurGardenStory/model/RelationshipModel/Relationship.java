package com.G2T7.OurGardenStory.model.RelationshipModel;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

/*
 * This class models the window to garden many-to-many relationship.
 * PK: WinId/Garden
 * SK: GardenName/WinId
 * WinId_GardenName: Win1|YishunGarden
 */

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@EqualsAndHashCode
@DynamoDBTable(tableName = "User_Win_Garden_Ballot_JoinTable")
public class Relationship {
  private String PK;
  private String SK;
  private String WinId_GardenName; // GSI

  @DynamoDBAttribute
  private String LeaseDuration;
  @DynamoDBAttribute
  private int NumPlotsForBalloting;
  @DynamoDBAttribute
  private String BallotId;
  @DynamoDBAttribute
  private String BallotDateTime;
  @DynamoDBAttribute
  private double Distance;
  @DynamoDBAttribute
  private String ballotStatus;

  // public enum BallotStatus {
  //   PENDING, SUCCESS, FAILED, INVALID
  // };

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

  @DynamoDBIndexHashKey(attributeName = "WinId_GardenName", globalSecondaryIndexName = "WinId_GardenName-index")
  public String getWinId_GardenName() {
    return this.WinId_GardenName;
  }

  public void setWinId_GardenName(String WinId_GardenName) {
    this.WinId_GardenName = WinId_GardenName;
  }

  public Relationship(String winId, String username, String winId_GardenName2, Object leaseDuration2, Object object,
      String ballotID2, String ballotDateTime2, double distance2, String ballotStatus2) {
  }
}
