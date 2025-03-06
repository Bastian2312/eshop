package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentRepository {
    private final Map<String, Payment> paymentMap;

    public PaymentRepository() {
        paymentMap = new ConcurrentHashMap<>();
    }

    public Payment save(Payment payment) {
        if(payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        paymentMap.put(payment.getId(), payment);
        return payment;
    }

    public Payment findById(String id) {
        return paymentMap.get(id);
    }

    public List<Payment> findAll() {
        return List.copyOf(paymentMap.values());
    }
}
