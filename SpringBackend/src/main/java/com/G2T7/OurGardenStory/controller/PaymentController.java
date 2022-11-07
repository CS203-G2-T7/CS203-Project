package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.service.PaymentService;
import com.stripe.model.Charge;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("https://ourgardenstory.me")
@RestController
@Api(value = "Payment Controller", description = "Operations pertaining to Payment model")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
    * Find out the payment details for a User, for all their ballots where ballotStatus is SUCCESS and paymentStatus is PENDING
    * If username does not belong to a registered user, throw AuthenticationCredentialsNotFoundException
    *
    * @param headers containing the username as a key
    * @return a JSONObject the amount, currency, and description of the payment
    */
    @ApiOperation(value = "Find all Payment details for a User")
    @GetMapping(path = "/payment")
    public ResponseEntity<?> findPayment(@RequestHeader Map<String, String> headers) {
        try {
            JSONObject chargeObject = paymentService.findCharge(headers.get("username"));
            if (chargeObject == null) {
                return ResponseEntity.badRequest().body("There are no ballots to be charged.");
            }
            return ResponseEntity.ok(chargeObject);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
    * Charges a User for all his outstanding ballot Charges
    * If username does not belong to a registered user, throw AuthenticationCredentialsNotFoundException
    *
    * @param headers containing the username as a key
    * @return a Charge object corresponding to the amount paid by User
    */
    @ApiOperation(value = "Charge User for the outstanding Ballot charges")
    @PostMapping(path = "/payment")
    public ResponseEntity<?> makePayment(@RequestHeader Map<String, String> headers) {
        try {
            Charge charge = paymentService.checkout(headers.get("username"));
            if (charge == null) {
                return ResponseEntity.badRequest().body("An error occurred while trying to create a charge.");
            }
            return ResponseEntity.ok("Amount paid is $" + charge.getAmount()/100.0 + " for " + charge.getDescription());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
