package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepositoryImpl();
        payment = Payment.builder()
                .id("1")
                .orderId("ORDER-123")
                .method("VOUCHER")
                .status("WAITING")
                .paymentData(Map.of("voucherCode", "ESHOP1234567890"))
                .build();
    }

    @Test
    void testSavePayment() {
        Payment savedPayment = paymentRepository.save(payment);
        assertEquals(payment, savedPayment);
    }

    @Test
    void testFindById() {
        paymentRepository.save(payment);
        Payment foundPayment = paymentRepository.findById("1");
        assertEquals(payment, foundPayment);
    }
}