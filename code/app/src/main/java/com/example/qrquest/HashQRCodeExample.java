package com.example.qrquest;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import java.util.Random;

public class HashQRCodeExample {

    // Dictionaries of words to choose from
    private static final String[][] DICTIONARIES = {
            {"cool", "hot"},
            {"Fro", "Glo"},
            {"Mo", "Lo"},
            {"Mega", "Ultra"},
            {"Spectral", "Sonic"},
            {"Crab", "Shark"}
    };


    // Function to generate a unique human-readable name for a QR code
    public static String generateQRCodeName(String hexString) {
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
}
