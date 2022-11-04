package com.G2T7.OurGardenStory.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Service
public class AlgorithmServiceImpl implements AlgorithmService {
    @Autowired
    private WinGardenService winGardenService;
    @Autowired
    private BallotService ballotService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
    @Autowired
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    private WindowService windowService;

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

    public void doMagic(String winId) {
        try {
            System.out.println("testing");
            List<Relationship> relationships = winGardenService.findAllGardensInWindow(winId);
            List<String> gardens = new ArrayList<>();

            for (Relationship r : relationships) {
                System.out.println(r.getSK());
                gardens.add(r.getSK());
            }

            System.out.println("======");

            for (String gardenName : gardens) {
                List<Relationship> ballots = ballotService.findAllBallotsInWindowGarden(winId, gardenName);
                HashMap<String, Double> usernameDistance = new HashMap<>();
                for (Relationship ballot : ballots) {
                    String username = ballot.getSK();
                    Double distance = ballot.getDistance();
                    usernameDistance.put(username, distance);
                }
                Relationship r = winGardenService.findGardenInWindow(winId, gardenName);
                int numPlotsAvailable = r.getNumPlotsForBalloting();
                ArrayList<String> ballotSuccesses = getBallotSuccess(usernameDistance,
                        numPlotsAvailable);
                for (Relationship ballot : ballots) {
                    if (ballotSuccesses.contains(ballot.getSK())) {
                        ballot.setBallotStatus("SUCCESS");
                        dynamoDBMapper.save(ballot);
                        String email = userService.findUserByUsername(ballot.getSK()).getEmail();
                        mailService.sendTextEmail(email, "SUCCESS"); // this throws IOException
                    } else {
                        ballot.setBallotStatus("FAIL");
                        dynamoDBMapper.save(ballot);
                        String email = userService.findUserByUsername(ballot.getSK()).getEmail();
                        mailService.sendTextEmail(email, "FAIL"); // this throws IOException
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scheduleAlgo(String winId) {
        Timer T = new Timer();
        TimerTask doAlgo = new TimerTask() {
            @Override
            public void run() {
                doMagic(winId);
            }
        };
        Window window = windowService.findWindowById(winId).get(0);
        String windowDuration = window.getWindowDuration();
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;

        if (Duration.parse(windowDuration) != null) {
            Duration d = Duration.parse(windowDuration);
            LocalDateTime endDate = LocalDateTime.now().plusSeconds(d.getSeconds());
            year = endDate.getYear();
            month = endDate.getMonthValue() - 1;
            day = endDate.getDayOfMonth();
            hour = endDate.getHour();
            minute = endDate.getMinute();
            second = endDate.getSecond();

        } else if (Period.parse(windowDuration) != null) {
            Period p = Period.parse(windowDuration);
            LocalDateTime endDate = LocalDateTime.now()
                                    .plusYears(p.getYears())
                                    .plusMonths(p.getMonths())
                                    .plusDays(p.getDays());
            
            year = endDate.getYear();
            month = endDate.getMonthValue() - 1;
            day = endDate.getDayOfMonth();
            hour = endDate.getHour();
            minute = endDate.getMinute();
            second = endDate.getSecond();
        }
        Calendar date = Calendar.getInstance();
        date.set(year, month, day, hour, minute, second);
        T.schedule(doAlgo, date.getTime());
        System.out.println(date.getTime().toString());
    }

    // public String getNextInWaitList (List<String> waitList) {
    //     String output = waitList.get(0);
    //     waitList.remove(0);
    //     return output;
    // }

    // public ArrayList<String> getWaitList (ArrayList<String> successes, HashMap<String,Double> balloters) {
    //     for (String success : successes) {
    //         balloters.remove(success);
    //     }
    //     int size = balloters.size();

    //     if (size >= 10) {
    //         return new AlgorithmServiceImpl().getBallotSuccess(balloters, 10);
    //     } 

    //     return new AlgorithmServiceImpl().getBallotSuccess(balloters, size);
    // }
}
