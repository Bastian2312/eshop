package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        payment = new Payment(
                "PAYMENT-123",
                "ORDER-123",
                "VOUCHER",
                "SUCCESS",
                Map.of("voucherCode", "ESHOP1234567890")
        );
    }

    @Test
    void testSavePayment() {
        Payment savedPayment = paymentRepository.save(payment);
        assertEquals(payment.getId(), savedPayment.getId());
        assertEquals(payment.getOrderId(), savedPayment.getOrderId());
        assertEquals("VOUCHER", savedPayment.getMethod());
    }

    @Test
    void testFindByIdFound() {
        paymentRepository.save(payment);
        Payment found = paymentRepository.findById("PAYMENT-123");
        assertEquals(payment, found);
    }

    @Test
    void testFindByIdNotFound() {
        Payment found = paymentRepository.findById("NON-EXISTENT");
        assertNull(found);
    }

    @Test
    void testFindAllPayments() {
        paymentRepository.save(payment);
        Payment payment2 = new Payment(
                "PAYMENT-456",
                "ORDER-456",
                "CASH_ON_DELIVERY",
                "WAITING",
                Map.of("address", "123 Main St")
        );
        paymentRepository.save(payment2);

        List<Payment> payments = paymentRepository.findAll();
        assertEquals(2, payments.size());
        assertTrue(payments.contains(payment));
        assertTrue(payments.contains(payment2));
    }
}