package com.G2T7.OurGardenStory.service;

import com.G2T7.OurGardenStory.model.RelationshipModel.Relationship;
import com.G2T7.OurGardenStory.model.Window;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.stripe.model.*;
import com.stripe.Stripe;
import com.stripe.param.CustomerSearchParams;

import net.minidev.json.JSONObject;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaymentService {
    @Value(value = "${stripe.keys.secret}")
    private String STRIPE_API_KEY;

    private final BallotService ballotService;
    private final WindowService windowService;
    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    public PaymentService(BallotService ballotService, WindowService windowService, DynamoDBMapper dynamoDBMapper) {
        this.ballotService = ballotService;
        this.windowService = windowService;
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * finds the charge object of user with a SUCCESS ballot status and PENDING payment status
     * Returns null if there are no such ballots
     *
     * @param username a String
     * @return JSONObject of charge object
     * JSONObject
     * 1:Amount
     * 2:Currency
     * 3:Description(WinId_GardenName)
     */
    public JSONObject findCharge(String username) {
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        String winId_GardenName = null;
        Map<String, Object> chargeParams = new HashMap<>();
        List<Window> windowList = windowService.findAllWindows();

        for (Window window : windowList) {
            try {
                Relationship ballot = ballotService.findUserBallotInWindow(window.getWindowId(), username);
                if (ballot.getBallotStatus().equals("SUCCESS") && ballot.getPaymentStatus().equals("PENDING")) {
                    winId_GardenName = ballot.getWinId_GardenName();
                    break;
                }
            } catch (ResourceNotFoundException e) {
                System.out.println(e); // not sure what to do in this catch
            }
        }

        if (winId_GardenName == null) {
            return null;
        }

        try {
            Stripe.apiKey = STRIPE_API_KEY;
            int amount = 6900; // amount is in cents
            chargeParams.put("amount", amount);
            chargeParams.put("currency", "sgd");
            chargeParams.put("description", winId_GardenName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONObject(chargeParams);
    }

    /**
     * Checkouts and makes payment on the ballot
     *
     * @param username a String
     * @return Charge a charge object
     * Charge
     * 1:Amount
     * 2:Currency
     * 3:Description(WinId_GardenName)
     * 4:Customer(Existing or new with its own payment details)
     */
    public Charge checkout(String username){
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        Charge charge = null;
        String customerId = null;
        CustomerSearchResult customerSearchResult = searchCustomer(username);
        if (!customerSearchResult.getData().isEmpty()) {
            customerId = customerSearchResult.getData().get(0).getId();
        }

        try {
            Stripe.apiKey = STRIPE_API_KEY;
            Map<String, Object> chargeParams = new HashMap<>();
            JSONObject chargeObject = findCharge(username);
            chargeParams.put("amount", chargeObject.getAsString("amount"));
            chargeParams.put("currency", chargeObject.getAsString("currency"));
            chargeParams.put("description", chargeObject.getAsString("description"));

            if (customerId != null) {
                chargeParams.put("customer", customerId);
            } else {
                chargeParams.put("customer", createCustomer(username).getId());
            }

            charge = Charge.create(chargeParams);
            String winId = chargeObject.getAsString("description").substring(0, 4);
            Relationship ballot = ballotService.findUserBallotInWindow(winId, username);
            ballot.setPaymentStatus("SUCCESS");
            dynamoDBMapper.save(ballot);

        } catch (NullPointerException e) {
            throw new NullPointerException("No ballot to be charged.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return charge;
    }

    /**
     * Creates customer if customer is new
     *
     * @param username a String
     * @return Customer
     * Customer:
     * 1:Name
     * 2:Source(payment_detail)
     */
    public Customer createCustomer(String username) {
        Map<String, Object> customerParams = new HashMap<>();
        Customer customer = null;

        try {
            Stripe.apiKey = STRIPE_API_KEY;
            customerParams.put("name", username);
            customerParams.put("source", "tok_mastercard");
            customer = Customer.create(customerParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
    }

    /**
     * Finds existing customer if any
     *
     * @param username a String
     * @return CustomerSearchResult
     */
    public CustomerSearchResult searchCustomer(String username) {
        CustomerSearchResult customerSearchResult = null;

        try {
            Stripe.apiKey = STRIPE_API_KEY;
            String query = "name: '" + username + "'";
            CustomerSearchParams customerSearchParams = CustomerSearchParams
                    .builder()
                    .setQuery(query)
                    .build();
            customerSearchResult = Customer.search(customerSearchParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return customerSearchResult;
    }
}