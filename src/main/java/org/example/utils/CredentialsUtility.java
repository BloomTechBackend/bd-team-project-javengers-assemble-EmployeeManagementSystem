package org.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for generating and verifying password hashes using the Argon2 algorithm.
 */
public class CredentialsUtility {
    private static final Logger log = LogManager.getLogger(CredentialsUtility.class);
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;

    /**
     * Generates a random salt using a secure random number generator.
     *
     * @return A base64-encoded string representing the generated salt.
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes a password using the Argon2 algorithm with the provided salt.
     *
     * @param password The password to hash.
     * @param salt     The salt to use in the hashing process.
     * @return A base64-encoded string representing the hashed password.
     */
    public static String hashPassword(String password, String salt) {
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        byte[] passwordBytes = password.getBytes();
        byte[] hash = new byte[HASH_LENGTH];

        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(saltBytes)
                .withParallelism(1)
                .withMemoryAsKB(65536)
                .withIterations(3)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);
        generator.generateBytes(passwordBytes, hash);

        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifies a password by comparing it to a previously computed hash.
     *
     * @param password The password to verify.
     * @param salt     The salt used in the original hashing process.
     * @param hash     The previously computed hash to compare against.
     * @return {@code true} if the password matches the hash, {@code false} otherwise.
     */
    public static boolean verifyPassword(String password, String salt, String hash) {
        String computedHash = hashPassword(password, salt);
        return computedHash.equals(hash);
    }

}
