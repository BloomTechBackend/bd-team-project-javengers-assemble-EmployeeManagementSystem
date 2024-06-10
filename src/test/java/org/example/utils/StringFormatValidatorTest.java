package org.example.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class StringFormatValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"19/hr", "27.50/hr", "6500/bw", "6550.25/bw"})
    public void validPayRateFormat_withValidPayRates_returnsTrue(String payRate) {
        boolean payRateFormatResult = StringFormatValidator.validPayRateFormat(payRate);

        assertTrue(payRateFormatResult, "Provided pay rate: " + payRate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"19hr", "27.50-hr", "35/HR", "6500/BW", "6550,25/bw", "8000/mo", "5500/MO",
            "6,500/bw", "6,500.1/bw", "6_500/bw", "$19/hr", "$1900/bw", "19/hr€"})
    public void validPayRateFormat_withInvalidPayRates_returnsFalse(String payRate) {
        boolean payRateFormatResult = StringFormatValidator.validPayRateFormat(payRate);

        assertFalse(payRateFormatResult, "Provided pay rate: " + payRate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345-1234", "12345"})
    public void validUnitedStatesZipCodeFormat_withValidZipCodes_returnsTrue(String zipCode) {
        boolean zipCodeFormatResult = StringFormatValidator.validUnitedStatesZipCodeFormat(zipCode);

        assertTrue(zipCodeFormatResult, "Provided Zip Code: " + zipCode);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "12", "123", "1234", "123456", "1234567", "12345678", "123456789", "1234567890",
            "12345-1", "12345-12", "12345-123", "12345-12345", "ABCDE", "ABCDE-FGHI", "!@#$%", "!@#$%-^&*("})
    public void validUnitedStatesZipCodeFormat_withInvalidZipCodes_returnsFalse(String zipCode) {
        boolean zipCodeFormatResult = StringFormatValidator.validUnitedStatesZipCodeFormat(zipCode);

        assertFalse(zipCodeFormatResult, "Provided Zip Code: " + zipCode);
    }

    @ParameterizedTest
    @ValueSource(strings = {"valid.email@example.com", "user.name+tag+sorting@example.com", "x@example.com",
            "example-indeed@strange-example.com", "example@example.com", "example@example.org", "example123@example.us",
            "example@example-domain-with-number123.com", "example_email@example.com"})
    public void validEmailFormat_withValidEmails_returnsTrue(String email) {
        boolean emailFormatResult = StringFormatValidator.validEmailFormat(email);

        assertTrue(emailFormatResult, "Provided Email: " + email);
    }

    @ParameterizedTest
    @ValueSource(strings = {"example|email@example.com", "example'email@example.org", ".email@email.com", "email@email",
    "email..example@example.com", "email.example@email..example.com", "example@example_email.com", "example@-email.com",
    "example@email-.com", "example.test.toplevel.domainabc"})
    public void validEmailFormat_withInvalidEmails_returnsFalse(String email) {
        boolean emailFormatResult = StringFormatValidator.validEmailFormat(email);

        assertFalse(emailFormatResult, "Provided Email: " + email);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John", "John Doe", "Mary-Jane Doe", "Mary Jane Joe", "John Doe-Adams", "Doe-Adams"})
    public void validNameFormat_withValidNames_returnsTrue(String name) {
        boolean validNameFormatResult = StringFormatValidator.validNameFormat(name);

        assertTrue(validNameFormatResult, "Provided Name: " + name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"J0hn", "John_Doe", "Mary-Jane Doe!", "Mary~Jane Joe", "John Doe--Adams", "Doe_-Adams"})
    public void validNameFormat_withInvalidNames_returnsFalse(String name) {
        boolean validNameFormatResult = StringFormatValidator.validNameFormat(name);

        assertFalse(validNameFormatResult, "Provided Name: " + name);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Valid1!Password",
            "Valid!Password123",
            "Valid_Password123!",
            "Valid-password@1bm",
            "Valid{Password}123",
            "Valid1@Password",
            "Valid`Password~123",
            "ValidPassword#123",
            "ValidPassword$123",
            "ValidPassword%123",
            "ValidPassword^123",
            "ValidPassword&123",
            "ValidPassword*123",
            "ValidPassword+123",
            "ValidPassword=123",
            "P@ssw0rd!"
    })
    void validPassword_withValidPasswords_returnsTrue(String password) {
        boolean validPasswordFormat = StringFormatValidator.validPassword(password);
        assertTrue(validPasswordFormat, "Provided Password: " + password);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "password",
            "Password",
            "PASSWORD",
            "pass123",
            "P@sswOrd",
            "12345678",
            "abcdefgh",
            "qwertyui",
            "!@#$%^&*",
            "p@$$w0rd",
            "P@ssword",
            "1234abcd",
            "qwerty12",
            "iloveyou",
            "letmein",
            "",
            "     "
    })
    void validPassword_withInvalidPasswords_returnsFalse(String password) {
        boolean validPasswordFormat = StringFormatValidator.validPassword(password);
        assertFalse(validPasswordFormat, "Provided Password: " + password);
    }

}
