package com.G2T7.OurGardenStory.model.RelationshipModel;

public class GardenWin extends Relationship {
  public GardenWin(String winId, String gardenName, String leaseDuration,
  int numPlotsForBalloting){
    super(winId, gardenName, winId + "_" + gardenName, leaseDuration,
    numPlotsForBalloting, null, null, 0, null);
  }
}
