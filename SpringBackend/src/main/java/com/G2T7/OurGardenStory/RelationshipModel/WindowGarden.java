package com.G2T7.OurGardenStory.RelationshipModel;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@DynamoDBTable(tableName = "User_Win_Garden_Ballot_JoinTable")
public class WindowGarden {
  /*
   * Window garden Many to many relationship. One garden have many window. One window have many garden.
   * Query patterns: Find gardens given WinId, Find latest WinId, then find gardens in latest WinId,
   * Find windows given GardenName.
   * 
   * Operations:
   * Create 
   * 
   * PK - SK - GardenName|WinId - LeaseDuration - NumPlotsForBalloting
   * Yishun Gardens - Win1 - Yishun Gardens|Win1 - 3Y - 30
   */

  private String PK;
  private String SK;

  @DynamoDBAttribute
  private String GardenAddress;
  @DynamoDBAttribute
  private String NumPlots;
  @DynamoDBAttribute
  private String Latitude;
  @DynamoDBAttribute
  private String Longitude;

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
}
