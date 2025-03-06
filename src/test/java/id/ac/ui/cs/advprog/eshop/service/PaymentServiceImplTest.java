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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;
    private Map<String, String> validVoucherData;
    private Map<String, String> invalidVoucherData;
    private Map<String, String> validCashData;
    private Map<String, String> invalidCashData;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId("ORDER-123");
        order.setStatus("WAITING_PAYMENT");

        validVoucherData = Map.of("voucherCode", "ESHOP1234567890");
        invalidVoucherData = Map.of("voucherCode", "INVALID");
        validCashData = Map.of("address", "123 Main St", "deliveryFee", "15000");
        invalidCashData = Map.of("address", "");
    }

    @Test
    void testAddPaymentWithValidVoucher() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), validVoucherData);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(order.getId(), result.getOrderId());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithInvalidVoucher() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), invalidVoucherData);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithValidCashOnDelivery() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), validCashData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithInvalidCashOnDelivery() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.CASH_ON_DELIVERY.getValue(), invalidCashData);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusToSuccessUpdatesOrder() {
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setStatus(PaymentStatus.WAITING.getValue());

        when(orderService.findById(order.getId())).thenReturn(order);

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(orderService).updateStatus(order.getId(), "SUCCESS");
    }

    @Test
    void testGetPaymentById() {
        Payment payment = new Payment();
        when(paymentRepository.findById("PAY-123")).thenReturn(payment);
        assertEquals(payment, paymentService.getPayment("PAY-123"));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());
        when(paymentRepository.findAll()).thenReturn(payments);
        assertEquals(2, paymentService.getAllPayments().size());
    }
}