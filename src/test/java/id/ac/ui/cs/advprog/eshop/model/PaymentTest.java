package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    private Map<String, String> voucherInfo;
    private Map<String, String> codInfo;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        voucherInfo = new HashMap<>();
        voucherInfo.put("voucherCode", "ESHOP2024DISC50");

        codInfo = new HashMap<>();
        codInfo.put("address", "Jl. Mawar No. 10");
        codInfo.put("deliveryFee", "15000");

        testOrder = new Order("ORDER12345", 250000);
    }

    @Test
    void testInvalidPaymentCreation() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Payment(null, voucherInfo, testOrder)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Payment("", voucherInfo, testOrder)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Payment("VOUCHER", null, testOrder)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Payment("COD", codInfo, null))
        );
    }

    @Test
    void testVoucherValidation() {
        Map<String, String> invalidVoucher = new HashMap<>();
        invalidVoucher.put("voucherCode", "INVALIDCODE123");
        Payment payment = new Payment("VOUCHER", invalidVoucher, testOrder);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCODValidation() {
        Map<String, String> invalidCOD = new HashMap<>();
        invalidCOD.put("address", "");
        invalidCOD.put("deliveryFee", "15000");
        Payment payment = new Payment("COD", invalidCOD, testOrder);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testInvalidPaymentMethod() {
        assertThrows(IllegalArgumentException.class, () -> new Payment("BITCOIN", voucherInfo, testOrder));
    }

    @Test
    void testSuccessfulPaymentCreation() {
        Payment voucherPayment = new Payment("VOUCHER", voucherInfo, testOrder);
        assertEquals("SUCCESS", voucherPayment.getStatus());
        assertSame(voucherInfo, voucherPayment.getPaymentData());
        assertSame(testOrder, voucherPayment.getOrder());

        Payment codPayment = new Payment("COD", codInfo, testOrder);
        assertEquals("SUCCESS", codPayment.getStatus());
        assertSame(codInfo, codPayment.getPaymentData());
        assertSame(testOrder, codPayment.getOrder());
    }
}
