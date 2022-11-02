package com.G2T7.OurGardenStory.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value(value = "${stripe.keys.secret}")
    private String STRIPE_API_KEY;

    // TODO: business logic of only successful ballots to make payment
    public String checkout(){
        String id = null;
        try {
            Stripe.apiKey = STRIPE_API_KEY;
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", 69);
            chargeParams.put("currency", "sgd");
            chargeParams.put("description", "Charge for testbuddy");
            chargeParams.put("source", "tok_mastercard");

            Charge charge = Charge.create(chargeParams);
            id = charge.getId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return id;
    }
}
