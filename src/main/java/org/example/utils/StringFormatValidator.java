package org.example.utils;

import java.util.regex.Pattern;

public class StringFormatValidator {
    private StringFormatValidator() {}

    /**
     * Checks whether the provided pay rate string matches a valid format for hourly or biweekly pay rates.<br>
     * <br>The valid formats include:
     * <ul>
     *     <li>
     *         Hourly pay rate: <code>number[.cents]/hr</code> where <code>number</code> is a non-negative integer or
     *     decimal and <code>cents</code> is optional and represents up to two decimal places.
     *     </li>
     *     <li>
     *         Biweekly pay rate: <code>number[.cents]/bw</code> where <code>number</code> is a non-negative integer or
     *         decimal and <code>cents</code> is optional and represents up to two decimal places.
     *      </li>
     * </ul>
     *
     * @param payRate the pay rate string to validate.
     * @return <code>true</code> if the provided pay rate string matches the valid format for hourly or biweekly pay rates,
     *         <code>false</code> otherwise.
     */
    public static boolean validPayRateFormat(String payRate) {
        Pattern hourlyPattern = Pattern.compile("^\\d+(\\.\\d{1,2}+)?/hr$");
        Pattern biweeklyPattern = Pattern.compile("^\\d+(\\.\\d{1,2}+)?/bw$");

        return hourlyPattern.matcher(payRate).find() || biweeklyPattern.matcher(payRate).find();
    }

    /**
     * Checks whether the provided string represents a valid United States zip code format.
     * A valid United States zip code can be either in the 5-digit format or the 5-digit-4 format.<br>
     * <br>Examples of valid formats:
     * <ul>
     *  <li>
     *      12345
     *  </li>
     *  <li>
     *      12345-6789
     *  </li>
     * </ul>
     *
     * @param zipcode the string to validate as a United States zip code.
     * @return <code>true</code> if the provided string represents a valid United States zip code format,
     *         <code>false</code> otherwise.
     */
    public static boolean validUnitedStatesZipCodeFormat(String zipcode) {
        Pattern unitedStatesZipCodePattern = Pattern.compile("^\\d{5}(-\\d{4})?$");
        return unitedStatesZipCodePattern.matcher(zipcode).find();
    }

    /**
     * Checks whether the provided string represents a valid email format.<br>
     * <br>A valid email address must follow the standard email format conventions, including:
     * <ul>
     *     <li>
     *         Starts with a sequence of one or more alphanumeric characters, underscores, dots, plus signs, asterisks,
     *         or hyphens.
     *     </li>
     *     <li>
     *         Followed by an "@" symbol.
     *     </li>
     *     <li>
     *         Followed by a domain name, which consists of one or more parts separated by dots, each part containing
     *         alphanumeric characters or hyphens.
     *     </li>
     *     <li>
     *         Ends with a top-level domain (TLD) consisting of 2 to 63 characters.
     *     </li>
     * </ul>
     *
     * @param email the string to validate as an email address.
     * @return <code>true</code> if the provided string represents a valid email format,
     *         <code>false</code> otherwise.
     */
    public static boolean validEmailFormat(String email) {
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*" +
                "@([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,63}$");
        return emailPattern.matcher(email).find();
    }

    /**
     * Checks whether the provided string represents a valid name format.
     * A valid name consists of one or more sequences of alphabetic characters,
     * optionally separated by spaces or hyphens.<br>
     * <br>Examples of valid names include:
     * <ul>
     *      <li>
     *          John Doe
     *      </li>
     *      <li>
     *          Mary-Ann
     *      </li>
     * </ul>
     *
     * @param name the string to validate as a name.
     * @return <code>true</code> if the provided string represents a valid name format,
     *         <code>false</code> otherwise.
     */
    public static boolean validNameFormat(String name) {
        Pattern namePattern = Pattern.compile("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");
        return namePattern.matcher(name).find();
    }

    /**
     * Checks whether the provided string represents a valid phone number format in the United States.<br>
     *
     * <br>A valid United States phone number must follow the format: +1-XXX-XXX-XXXX, where X represents a digit.
     *
     * @param phoneNumber the string to validate as a phone number.
     * @return <code>true</code> if the provided string represents a valid United States phone number format,
     *         <code>false</code> otherwise.
     */
    public static boolean validUsPhoneFormat(String phoneNumber) {
        Pattern unitedStatesPhoneNumberPattern = Pattern.compile("\\+1-\\d{3}-\\d{3}-\\d{4}");
        return unitedStatesPhoneNumberPattern.matcher(phoneNumber).find();
    }
}
