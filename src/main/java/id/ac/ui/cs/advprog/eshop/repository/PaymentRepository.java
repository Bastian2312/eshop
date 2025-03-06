package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class PaymentRepository {
    private Map<String, Payment> paymentMap = new HashMap<>();

    public Payment save(Payment payment) {
        paymentMap.put(payment.getId(), payment);
        return payment;
    }

    public Payment findById(String id) {
        return paymentMap.get(id);
    }

    public List<Payment> findAll() {
        return new ArrayList<>(paymentMap.values());
    }
}