package com.G2T7.OurGardenStory.Payment;

import com.G2T7.OurGardenStory.service.PaymentService;
import com.stripe.model.Charge;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.stripe.model.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Test
    public void createCustomer_newCustomer_returnCustomer(){
        Customer customer = paymentService.createCustomer("John");

        assertNotNull(customer);
    }

    @Test
    public void createCheckout_newCharge_returnCharge(){
        Charge checkoutObject = paymentService.checkout("John");

        assertNotNull(checkoutObject);
    }
}
