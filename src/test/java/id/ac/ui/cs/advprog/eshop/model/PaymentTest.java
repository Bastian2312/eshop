package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234567890");
    }

    @Test
    void testCreatePaymentValid() {
        Payment payment = new Payment("1", "ORDER-123", "VOUCHER", "SUCCESS", paymentData);

        assertEquals("1", payment.getId());
        assertEquals("ORDER-123", payment.getOrderId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("1", "ORDER-123", "INVALID", "SUCCESS", paymentData);
        });
    }

    @Test
    void testSetInvalidStatus() {
        Payment payment = new Payment("1", "ORDER-123", "VOUCHER", "SUCCESS", paymentData);
        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("INVALID"));
    }
}