package id.ac.ui.cs.advprog.eshop.validator;

import java.util.Map;

public class CashOnDeliveryValidator {
    public static boolean isValid(Map<String, String> paymentData) {
        if (paymentData == null) return false;

        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");
        return address != null && !address.isEmpty() &&
                deliveryFee != null && !deliveryFee.isEmpty();
    }
}