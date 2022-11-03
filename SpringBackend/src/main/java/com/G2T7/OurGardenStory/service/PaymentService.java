package com.G2T7.OurGardenStory.service;

import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.param.CustomerSearchParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.Charge;

import java.util.*;

@Service
public class PaymentService {
    @Value(value = "${stripe.keys.secret}")
    private String STRIPE_API_KEY;

    // TODO: business logic of only successful ballots to make payment
    public String checkout(String username){
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }

        String chargeId = null;
        String customerId = null;
        CustomerSearchResult result = searchCustomer(username);
        if (!result.getData().isEmpty()) {
            customerId = result.getData().get(0).getId();
        }

        try {
            Stripe.apiKey = STRIPE_API_KEY;
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", 69);
            chargeParams.put("currency", "sgd");
            chargeParams.put("description", "Charge for ballot at ");

            if (customerId != null) {
                chargeParams.put("customer", customerId);
            } else {
                chargeParams.put("customer", createCustomer(username).getId());
            }

            Charge charge = Charge.create(chargeParams);
            chargeId = charge.getId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return chargeId;
    }

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

    public CustomerSearchResult searchCustomer(String username) {
        CustomerSearchResult result = null;

        try {
            Stripe.apiKey = STRIPE_API_KEY;
            String query = "name: '" + username + "'";
            CustomerSearchParams customerSearchParams = CustomerSearchParams
                    .builder()
                    .setQuery(query)
                    .build();
            result = Customer.search(customerSearchParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
