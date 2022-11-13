package com.G2T7.OurGardenStory.Payment;

import com.G2T7.OurGardenStory.service.BallotService;
import com.G2T7.OurGardenStory.service.PaymentService;
import com.G2T7.OurGardenStory.service.WindowService;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.stripe.model.Charge;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.stripe.model.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @InjectMocks
    WindowService windowService;

    @InjectMocks
    BallotService ballotService;

    @Mock
    private DynamoDBMapper mapperMock = mock(DynamoDBMapper.class);

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

    @Test
    public void findCharge_haveCharge_returnCharge(){
        PaymentService paymentService1 = new PaymentService(ballotService, windowService, mapperMock);
        JSONObject chargeObject = paymentService1.findCharge("John");

        assertNull(chargeObject);
    }
}
