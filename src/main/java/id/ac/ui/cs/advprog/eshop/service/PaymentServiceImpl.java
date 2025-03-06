package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.validator.CashOnDeliveryValidator;
import id.ac.ui.cs.advprog.eshop.validator.VoucherValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .orderId(order.getId())
                .method(method)
                .status(determineInitialStatus(method, paymentData))
                .paymentData(new HashMap<>(paymentData))
                .build();

        return paymentRepository.save(payment);
    }

    private String determineInitialStatus(String method, Map<String, String> paymentData) {
        if (PaymentMethod.VOUCHER.getValue().equals(method)) {
            return VoucherValidator.isValid(paymentData.get("voucherCode"))
                    ? PaymentStatus.SUCCESS.getValue()
                    : PaymentStatus.REJECTED.getValue();
        }

        if (PaymentMethod.CASH_ON_DELIVERY.getValue().equals(method)) {
            return CashOnDeliveryValidator.isValid(paymentData)
                    ? PaymentStatus.SUCCESS.getValue()
                    : PaymentStatus.REJECTED.getValue();
        }

        return PaymentStatus.WAITING.getValue();
    }

    // Removed validation methods

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        Payment updatedPayment = paymentRepository.save(payment);

        if (PaymentStatus.SUCCESS.getValue().equals(status)) {
            orderService.updateStatus(payment.getOrderId(), "SUCCESS");
        } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
            orderService.updateStatus(payment.getOrderId(), "FAILED");
        }

        return updatedPayment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}