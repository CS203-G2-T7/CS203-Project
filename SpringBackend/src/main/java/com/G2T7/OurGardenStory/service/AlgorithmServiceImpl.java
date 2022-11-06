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
public class AlgorithmServiceImpl {
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

    /**
    * This method is an algorithm that chooses a number of successful ballots our of all ballots for a garden and window.
    * The algorithm takes into account the distance between the balloter and the garden, where the 
    * shorter the distance between the user and the garden, the more likely their ballot will be successful.
    *
    * @param balloters a map with the key being the username, and the corresponding value being the distance between the user's address
    *                  and the address of the garden that the ballot is for
    * @param numSuccess the maximum number of successful ballots
    * @return a list of all successful balloters for a GardenWindow
    */
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

    /**
    * For a window, get all the successful ballots for all the gardens in the window, 
    * update the ballot status for all balloters (SUCCESS or FAIL), and send the 
    * respective emails.
    *
    * @param winId a String
    * @return no content
    */
    public void doMagic(String winId) {
        try {
            List<Relationship> relationships = winGardenService.findAllGardensInWindow(winId);
            List<String> gardens = new ArrayList<>();

            for (Relationship r : relationships) {
                System.out.println(r.getSK());
                gardens.add(r.getSK());
            }

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
                        String username = userService.findUserByUsername(ballot.getSK()).getSK();
                        mailService.sendTextEmail(email, username, "SUCCESS", ballot.getWinId_GardenName()); // this throws IOException
                    } else {
                        ballot.setBallotStatus("FAIL");
                        dynamoDBMapper.save(ballot);
                        String email = userService.findUserByUsername(ballot.getSK()).getEmail();
                        String username = userService.findUserByUsername(ballot.getSK()).getSK();
                        mailService.sendTextEmail(email, username, "FAIL", ballot.getWinId_GardenName()); // this throws IOException
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Schedules doMagic() to be called at a particular time for a window. This time will be the startDate of the window,
    * added with the windowDuration.
    *
    * @param winId a String
    * @return no content
    */
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

        if (windowDuration.charAt(1) == 'T') {
            Duration d = Duration.parse(windowDuration);
            LocalDateTime endDate = LocalDateTime.now().plusSeconds(d.getSeconds());
            year = endDate.getYear();
            month = endDate.getMonthValue() - 1;
            day = endDate.getDayOfMonth();
            hour = endDate.getHour();
            minute = endDate.getMinute();
            second = endDate.getSecond();

        } else if (windowDuration.charAt(1) != 'T') {
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
}
