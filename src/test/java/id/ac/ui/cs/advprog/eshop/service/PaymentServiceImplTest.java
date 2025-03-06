package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {  // Renamed to match implementation class
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        order = new Order("ORDER-123", new ArrayList<>(), 1708560000L, "John Doe");
        paymentData = new HashMap<>();
    }

    @Test
    void testAddValidVoucherPayment() {
        paymentData.put("voucherCode", "ESHOP1234567890");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddInvalidVoucherPayment() {
        paymentData.put("voucherCode", "INVALID_CODE");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddValidCashOnDelivery() {
        paymentData.put("address", "123 Main St");
        paymentData.put("deliveryFee", "15000");
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddInvalidCashOnDelivery() {
        paymentData.put("address", "");
        paymentData.put("deliveryFee", null);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testSetStatusSuccessUpdatesOrder() {
        Payment payment = Payment.builder()
                .id("PAY-123")
                .orderId("ORDER-123")
                .method("VOUCHER")
                .status("WAITING")
                .build();

        when(orderService.getOrderById("ORDER-123")).thenReturn(order);

        paymentService.setStatus(payment, "SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }
}