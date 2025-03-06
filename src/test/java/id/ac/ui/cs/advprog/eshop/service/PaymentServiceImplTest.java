package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderServiceImpl orderService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId("ORDER-123");
        order.setStatus("WAITING_PAYMENT");
    }

    @Test
    void testAddPaymentWithValidVoucher() {
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234567890");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), paymentData);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(order.getId(), result.getOrderId());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithInvalidVoucher() {
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALID_CODE");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithValidCashOnDelivery() {
        paymentData = new HashMap<>();
        paymentData.put("address", "123 Main St");
        paymentData.put("deliveryFee", "15000");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentData);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithInvalidCashOnDelivery() {
        paymentData = new HashMap<>();
        paymentData.put("address", "");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), paymentData);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusToSuccessUpdatesOrder() {
        Payment payment = Payment.builder()
                .id("PAY-001")
                .orderId(order.getId())
                .status(PaymentStatus.WAITING.getValue())
                .build();

        when(orderService.getOrderById(order.getId())).thenReturn(order);

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals("SUCCESS", order.getStatus());
        verify(orderService).updateOrder(order);
    }

    @Test
    void testGetPaymentById() {
        Payment payment = new Payment();
        when(paymentRepository.findById("PAY-001")).thenReturn(payment);

        Payment result = paymentService.getPayment("PAY-001");
        assertEquals(payment, result);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = List.of(new Payment(), new Payment());
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
    }
}