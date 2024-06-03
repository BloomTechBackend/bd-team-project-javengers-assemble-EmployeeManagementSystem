package org.example.utils;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class CredentialsUtilityTest {
    @Test
    public void generateSalt_createsSaltOfCorrectLength() {
        String salt = CredentialsUtility.generateSalt();
        assertNotNull(salt);
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        assertEquals(16, saltBytes.length);
    }

    @Test
    public void hashPassword_generatesConsistentHash() {
        String password = "TestPassword123!";
        String salt = CredentialsUtility.generateSalt();

        String hash1 = CredentialsUtility.hashPassword(password, salt);
        String hash2 = CredentialsUtility.hashPassword(password, salt);

        assertNotNull(hash1);
        assertNotNull(hash2);
        assertEquals(hash1, hash2);
    }

    @Test
    public void hashPassword_generatesDifferentHashesForDifferentSalts() {
        String password = "TestPassword123!";
        String salt1 = CredentialsUtility.generateSalt();
        String salt2 = CredentialsUtility.generateSalt();

        String hash1 = CredentialsUtility.hashPassword(password, salt1);
        String hash2 = CredentialsUtility.hashPassword(password, salt2);

        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void verifyPassword_correctlyValidatesPassword() {
        String password = "TestPassword123!";
        String salt = CredentialsUtility.generateSalt();
        String hash = CredentialsUtility.hashPassword(password, salt);

        assertTrue(CredentialsUtility.verifyPassword(password, salt, hash));
    }

    @Test
    public void verifyPassword_failsForIncorrectPassword() {
        String password = "TestPassword123!";
        String incorrectPassword = "WrongPassword!";
        String salt = CredentialsUtility.generateSalt();
        String hash = CredentialsUtility.hashPassword(password, salt);

        assertFalse(CredentialsUtility.verifyPassword(incorrectPassword, salt, hash));
    }

    @Test
    public void verifyPassword_failsForIncorrectHash() {
        String password = "TestPassword123!";
        String salt = CredentialsUtility.generateSalt();
        String hash = CredentialsUtility.hashPassword(password, salt);
        String incorrectHash = CredentialsUtility.hashPassword("WrongPassword!", salt);

        assertFalse(CredentialsUtility.verifyPassword(password, salt, incorrectHash));
    }

    @Test
    public void verifyPassword_failsForDifferentSalt() {
        String password = "TestPassword123!";
        String salt1 = CredentialsUtility.generateSalt();
        String salt2 = CredentialsUtility.generateSalt();
        String hash = CredentialsUtility.hashPassword(password, salt1);

        assertFalse(CredentialsUtility.verifyPassword(password, salt2, hash));
    }
}
