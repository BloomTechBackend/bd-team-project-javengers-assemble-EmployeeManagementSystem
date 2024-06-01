package org.example.utils;

import java.util.regex.Pattern;

public class StringFormatValidator {
    private StringFormatValidator() {}

    public static boolean validPayrateFormat(String payrate) {
        Pattern hourlyPattern = Pattern.compile("\\b\\d+(\\.\\d+)?/hr\\b");
        Pattern biweeklyPattern = Pattern.compile("\\b\\d+(\\.\\d+)?/bw\\b");

        return hourlyPattern.matcher(payrate).find() || biweeklyPattern.matcher(payrate).find();
    }

    public static boolean validUnitedStatesZipCodeFormat(String zipcode) {
        Pattern unitedStatesZipCodePattern = Pattern.compile("^\\d{5}(-\\d{4})?$");
        return unitedStatesZipCodePattern.matcher(zipcode).find();
    }

    public static boolean validEmailFormat(String email) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,7}$");
        return emailPattern.matcher(email).find();
    }

    /**
     * Validates the format of a name, ensuring it contains only alphabetic characters (both lowercase and uppercase),
     * hyphens, and single spaces between words. This method can be used to validate any name, including hyphenated names
     * and names with spaces.
     *
     * @param name the name to be validated
     * @return {@code true} if the name is valid; {@code false} otherwise
     */
    public static boolean validNameFormat(String name) {
        Pattern namePattern = Pattern.compile("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");
        return namePattern.matcher(name).find();
    }

    public static boolean validUsPhoneFormat(String phoneNumber) {
        Pattern unitedStatesPhoneNumberPattern = Pattern.compile("\\+1-\\d{3}-\\d{3}-\\d{4}");
        return unitedStatesPhoneNumberPattern.matcher(phoneNumber).find();
    }
}
