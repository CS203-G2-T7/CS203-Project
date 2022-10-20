package com.G2T7.OurGardenStory.repository;

import com.G2T7.OurGardenStory.controller.GeocodeController;
import com.G2T7.OurGardenStory.geocoder.AlgorithmServiceImpl;
import com.G2T7.OurGardenStory.model.Ballot;
import com.G2T7.OurGardenStory.model.Garden;
import com.G2T7.OurGardenStory.model.UserSignInResponse;
import com.G2T7.OurGardenStory.model.Window;
import com.G2T7.OurGardenStory.service.MailService;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Repository
public class BallotRepo {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private WindowRepo windowRepo;

    @Autowired
    private GardenRepo gardenRepo;

    @Autowired
    private GeocodeController geocodeController; // JH: why is controller here?

    @Autowired
    private MailService mailService;

    public String[] getPayloadAttributes() {
        String idToken = UserSignInResponse.getIdToken();
        String[] chunks = idToken.split("\\."); // chunk 0 is header, chunk 1 is payload, chunk 2 is signature

        Base64.Decoder decoder = Base64.getUrlDecoder(); // Decode via Base64
        String decodedPayload = new String(decoder.decode(chunks[1])); // gets payload from idToken
        String[] payload_attr = decodedPayload.split(",");

        return payload_attr;
    }

    public String findPostCodeByIdToken(String[] payload_attr) {
        for (String payload : payload_attr) {
            if (payload.contains("address")) {
                return payload.substring(payload.indexOf(":\"") + 2, payload.indexOf("\"}")); // address is returned
            }
        }
        return null;
    }

    public String findUsernameByIdToken(String[] payload_attr) {
        for (String payload : payload_attr) {
            if (payload.contains("username")) {
                return payload.substring(payload.indexOf(":\"") + 2, payload.length() - 1); // username is returned
            }
        }
        return null;
    }

    public String findEmailByIdToken(String[] payload_attr) {
        for (String payload : payload_attr) {
            if (payload.contains("email\"")) {
                return payload.substring(payload.indexOf(":\"") + 2, payload.length() - 2); // email is returned
            }
        }
        return null;
    }

    public Ballot save(String gardenName) {
        // List<String> gardenListNames = gardenRepo.listGardenNames();
        // if (!gardenListNames.contains(gardenName)) {
        //     return null;
        // }

        // String username = findUsernameByIdToken(getPayloadAttributes());
        // if (checkIfBalloted(username)) {
        //     return null;
        // }

        // Ballot ballot = new Ballot();
        // ballot.setSubmitDateTime(LocalDateTime.now());
        // Window window = windowRepo.findLatestWindow();
        // ballot.setStartDateTime(window.getStartDateTime());
        // ballot.setLeaseStart(window.getLeaseStart());
        // ballot.setNumBidsPlaced(getBidsPlaced(gardenName));
        // String postCode = "Singapore " + findPostCodeByIdToken(getPayloadAttributes()); // add Singapore prefix to
        //                                                                                 // address
        // Garden garden = gardenRepo.getGardenByGardenName(gardenName);
        // Double distance = geocodeController.saveDistance(username, postCode, garden.getLongitude(),
        //         garden.getLatitude()); // JH: very very bad
        // ballot.setDistance(distance);
        // ballot.setUsername(username);
        // ballot.setEmail(findEmailByIdToken(getPayloadAttributes()));
        // ballot.setGarden(garden);
        // dynamoDBMapper.save(ballot);
        return new Ballot();
    }

    public List<Ballot> listBallots() {
        return dynamoDBMapper.scan(Ballot.class, new DynamoDBScanExpression());
    }

    public List<Ballot> listBallotsFromLatestWindow() {
        Window window = windowRepo.findLatestWindow();
        List<Ballot> ballotList = listBallots();
        List<Ballot> returnBallotList = new ArrayList<>();
        for (Ballot ballot : ballotList) {
            if (ballot.getStartDateTime().equals(window.getStartDateTime())) {
                returnBallotList.add(ballot);
            }
        }
        return returnBallotList;
    }

    public boolean checkIfBalloted(String username) {
        for (Ballot ballot : listBallotsFromLatestWindow()) {
            if (ballot.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    public int getBidsPlaced(String gardenName) {
        // int count = 0;
        // for (Ballot ballot : listBallotsFromLatestWindow()) {
        //     if (ballot.getGarden().getName().equals(gardenName)) {
        //         count += 1;
        //     }
        // }

        return 1;
    }

    //this calls algo, updates status, send email
    public List<Ballot> doMagic() {
        List<Ballot> returnBallotList = new ArrayList<>();

        for (Garden garden : windowRepo.findLatestWindow().getGardenList()) {
            System.out.println(garden);
            List<Ballot> ballotList = callAlgo(garden);
            for (Ballot ballot : ballotList) {
                System.out.println(ballot);
                returnBallotList.add(ballot);
            }
        }

        return returnBallotList;
    }

    public List<Ballot> callAlgo(Garden garden) {
        // AlgorithmServiceImpl algo = new AlgorithmServiceImpl();
        // HashMap<String, Double> ballotters = new HashMap<>();
        // //get the hashmap for ballots from window from garden
        // for (Ballot ballot : listBallotsFromLatestWindow()) {
        //     if (ballot.getGarden().getName().equals(garden.getName())) {
        //         ballotters.put(ballot.getUsername(), ballot.getDistance());
        //     }
        // }
        // ArrayList<String> output = new ArrayList<>();
        // //gets the successful balloters
        // output = algo.getBallotSuccess(ballotters, 1);
        // List<Ballot> ballotListForWindow = listBallotsFromLatestWindow();
        // List<Ballot> ballotListForWindowForGarden = new ArrayList<>();
        // List<Ballot> returnBallotList = new ArrayList<>();
        
        // //gets all the balloters for that window for that garden
        // for (Ballot ballot : ballotListForWindow) {
        //     try {
        //         if (ballot.getGarden().getName().equals(garden.getName())) {
        //             ballotListForWindowForGarden.add(ballot);
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }

        // //changes the status of successful ballots to success, sends email
        // changeStatusSuccess(ballotListForWindowForGarden, output, returnBallotList);

        // //changes the remaining balloters to fail, sends email
        // changeStatusFail(ballotListForWindowForGarden, returnBallotList);

        return new ArrayList<Ballot>(); //return returnBallotList;
    }

    // private void changeStatusSuccess(List<Ballot> ballotListForWindowForGarden, List<String> output, List<Ballot> returnBallotList) {
    //     for (Ballot ballot : ballotListForWindowForGarden) {
    //         System.out.println(ballot.getStatus() + " " + ballot.getUsername());
    //         for (String success : output) {
    //             if (success.equals(ballot.getUsername())) {
    //                 ballot.setStatus("Successful :)");
    //                 try {
    //                     mailService.sendSuccessTextEmail(ballot.getEmail());
    //                 } catch (Exception e) {
    //                     e.printStackTrace();
    //                 }
    //                 dynamoDBMapper.save(ballot);
    //                 returnBallotList.add(ballot);
    //             }
    //         }
    //     }
    // }

    // private void changeStatusFail(List<Ballot> ballotListForWindowForGarden, List<Ballot> returnBallotList) {
    //     for (Ballot ballot : ballotListForWindowForGarden) {
    //         if (ballot.getStatus().equals("PENDING")) {
    //             ballot.setStatus("Unsuccessful :(");
    //             try {
    //                 mailService.sendFailureTextEmail(ballot.getEmail());
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //             }
    //             dynamoDBMapper.save(ballot);
    //             returnBallotList.add(ballot);
    //         }
    //     }
    // }
}
