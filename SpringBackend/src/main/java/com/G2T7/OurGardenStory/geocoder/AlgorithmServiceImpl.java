package com.G2T7.OurGardenStory.geocoder;

import java.util.*;

public class AlgorithmServiceImpl implements AlgorithmService {
    public ArrayList<String> getBallotSuccess(HashMap<String,Double> balloters, int numSuccess) {
        ArrayList<String> list = new ArrayList<>(balloters.keySet());
        for (String key : balloters.keySet()) {
            if (balloters.get(key) <= 2) {
                list.add(key);
                list.add(key);
                list.add(key);
                list.add(key);
            }
            if (balloters.get(key) <= 5 && balloters.get(key) > 2) {
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

    public String getNextInWaitList (List<String> waitList) {
        String output = waitList.get(0);
        waitList.remove(0);
        return output;
    }

    public ArrayList<String> getWaitList (ArrayList<String> successes, HashMap<String,Double> balloters) {
        for (String success : successes) {
            balloters.remove(success);
        }
        int size = balloters.size();

        if (size >= 10) {
            return new AlgorithmServiceImpl().getBallotSuccess(balloters, 10);
        } 

        return new AlgorithmServiceImpl().getBallotSuccess(balloters, size);
    }
}
