package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentRepository {
    private Map<String, Payment> paymentData;

    public PaymentRepository() {
        paymentData = new HashMap<>();
    }

    public Payment save(Payment payment) {
        paymentData.put(payment.getId(), payment);
        return payment;
    }

    public Payment findById(String id) {
        return paymentData.get(id);
    }

    public List<Payment> findAll() {
        return new ArrayList<>(paymentData.values());
    }
}