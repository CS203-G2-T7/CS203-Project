package com.G2T7.OurGardenStory.controller;

import com.G2T7.OurGardenStory.service.PaymentService;
import com.stripe.model.Charge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

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
