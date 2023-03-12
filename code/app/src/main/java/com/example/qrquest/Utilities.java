package com.example.qrquest;


import androidx.annotation.NonNull;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * This class consists of different functions that are utilised in the project.
 * @author Zijing Lu
 * @author Jay Pasrija
 * @author Michael Wolowyk
 * */
public class Utilities {

    // Dictionaries of words to choose from
    private final String[][] DICTIONARIES = {
            {"Freezing", "Magma"},
            {"Spirit", "Spark"},
            {"ing", "ling"},
            {"Tiny", "Huge"},
            {"Common", "Legendary"},
            {"Monster", "Beast"}
    };

    // Function to generate a unique human-readable name for a QR code
    @NonNull
    public String hashName(@NonNull String hexString) {
        // Convert the hex string to a byte array
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);

        // Compute the SHA-256 hash of the byte array using Guava's Hashing.sha256()
        String hashString = Hashing.sha256().hashBytes(byteArray).toString();

        // Generate a name using the first 6 bits of the hash
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int dictionaryIndex = i % DICTIONARIES.length;
            String[] dictionary = DICTIONARIES[dictionaryIndex];
            int wordIndex = (hashString.charAt(i) & 0x01) % dictionary.length;
            nameBuilder.append(dictionary[wordIndex]);
        }

        return nameBuilder.toString();
    }

    // Function to generate a score based on the hash of the qr code
    public int hashScore(@NonNull String hexString) {
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);

        int score = 0;
        for (byte b : byteArray) {
            score += b;
        }
        return score;
    }
}
