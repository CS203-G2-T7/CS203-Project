package com.G2T7.OurGardenStory.service;

import java.util.*;

import org.springframework.stereotype.Service;

public interface AlgorithmService {
    
    //get the username of the successful ballots of a window for a garden
    public ArrayList<String> getBallotSuccess(HashMap<String,Double> balloters, int numSuccess);

    // //get the waitList, which are the next 10 people (or less) who just missed out
    // public ArrayList<String> getWaitList (ArrayList<String> successes, HashMap<String,Double> balloters);

    // //get the username of the next in line in the waitList
    // public String getNextInWaitList (List<String> waitList);
}
