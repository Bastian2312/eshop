package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.*;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order(
                "ORDER-123",
                products,
                System.currentTimeMillis(),
                "Author Name"
        );
    }

    @Test
    void testAddPaymentWithValidVoucher() {
        Map<String, String> paymentData = new HashMap<>();
        // Valid 16-character voucher with exactly 8 digits
        paymentData.put("voucherCode", "ESHOP12A34B56C78");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> inv.getArgument(0));

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), paymentData);

        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testAddPaymentWithInvalidVoucher() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALID_CODE");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        Payment result = paymentService.addPayment(order, PaymentMethod.VOUCHER.getValue(), paymentData);

        assertNotNull(result);
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusSuccessUpdatesOrder() {
        Payment payment = Payment.builder()
                .id("PAY-123")
                .orderId(order.getId())
                .method(PaymentMethod.VOUCHER.getValue())
                .status(PaymentStatus.WAITING.getValue())
                .paymentData(Map.of("voucherCode", "ESHOP1234ABCD5678"))
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderService.updateStatus(order.getId(), "SUCCESS")).thenReturn(order);

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertNotNull(result);
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        verify(orderService).updateStatus(order.getId(), "SUCCESS");
    }

    @Test
    void testGetPaymentById() {
        Payment expected = Payment.builder()
                .id("PAY-123")
                .orderId("ORDER-123")
                .method(PaymentMethod.VOUCHER.getValue())
                .status(PaymentStatus.SUCCESS.getValue())
                .paymentData(Map.of("voucherCode", "ESHOP1234ABCD5678"))
                .build();

        when(paymentRepository.findById("PAY-123")).thenReturn(expected);

        Payment actual = paymentService.getPayment("PAY-123");
        assertEquals(expected, actual);
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(
                Payment.builder()
                        .id("PAY-1")
                        .orderId("ORDER-123")
                        .method(PaymentMethod.VOUCHER.getValue())
                        .status(PaymentStatus.SUCCESS.getValue())
                        .paymentData(Map.of("voucherCode", "ESHOP1234ABCD5678"))
                        .build(),
                Payment.builder()
                        .id("PAY-2")
                        .orderId("ORDER-456")
                        .method(PaymentMethod.CASH_ON_DELIVERY.getValue())
                        .status(PaymentStatus.REJECTED.getValue())
                        .paymentData(Map.of("address", "123 St", "deliveryFee", "10000"))
                        .build()
        );

        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
    }
}