package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Builder
@Getter
public class Payment {
    private String id;
    private String orderId;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String orderId, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        this.orderId = orderId;

        if (!isValidMethod(method)) {
            throw new IllegalArgumentException();
        }
        this.method = method;

        setStatus(status);
        this.paymentData = paymentData;
    }

    public void setStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }

    private boolean isValidMethod(String method) {
        return method.equals("VOUCHER") || method.equals("CASH_ON_DELIVERY");
    }

    private boolean isValidStatus(String status) {
        return status.equals("SUCCESS") || status.equals("REJECTED") || status.equals("WAITING");
    }
}