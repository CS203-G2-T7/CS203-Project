package com.G2T7.OurGardenStory.geocoder;

import java.util.*;

public class Algorithm {

    public static ArrayList<String> getBallotSuccess(HashMap<String,Double> ballotters, int numSuccess) {
        ArrayList<String> list = new ArrayList<>(ballotters.keySet());
        for (String key : ballotters.keySet()) {
            if (ballotters.get(key) <= 2) {
                list.add(key);
                list.add(key);
                list.add(key);
                list.add(key);
            }
            if (ballotters.get(key) <= 5 && ballotters.get(key) > 2) {
                list.add(key);
                list.add(key);
            }
        }
        ArrayList<String> output = new ArrayList<>();

        for (int i = 0; i < numSuccess; i++) {
            int size = list.size();
            Random rand = new Random();
            int idx = rand.nextInt(size);
            output.add(list.get(idx));
            list.removeIf(name -> name.equals(list.get(idx)));

        }

        return output;
    }
}
