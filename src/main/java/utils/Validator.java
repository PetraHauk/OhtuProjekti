package utils;

import java.util.regex.Pattern;

public class Validator {

    public boolean validateEmail(String email) {
        // Regex to allow Unicode characters in the local part and domain.
        String emailRegex = "^[\\p{L}0-9_+&*-]+(?:\\.[\\p{L}0-9_+&*-]+)*@(?:[\\p{L}0-9-]+\\.)+[\\p{L}]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\+?[0-9]{1,4}?[-.\\s]?([0-9]{1,3}[-.\\s]?)?[0-9]{4,14}$";
        return phoneNumber.matches(phoneRegex);
    }
}
