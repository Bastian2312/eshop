package id.ac.ui.cs.advprog.eshop.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    VOUCHER("VOUCHER"),
    CASH_ON_DELIVERY("CASH_ON_DELIVERY");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public static boolean contains(String method) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.getValue().equals(method)) {
                return true;
            }
        }
        return false;
    }
}