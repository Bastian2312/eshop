package id.ac.ui.cs.advprog.eshop.validator;

public class VoucherValidator {
    public static boolean isValid(String voucherCode) {
        if (voucherCode == null) return false;

        return voucherCode.startsWith("ESHOP") &&
                voucherCode.length() == 16 &&
                voucherCode.chars().filter(Character::isDigit).count() == 8;
    }
}