package com.example.qrquest;

import java.nio.charset.StandardCharsets;
import com.google.common.hash.Hashing;
import java.util.Random;
public class HashQRCode {

    // Array of names to choose from
    private static final String[] nameArray = {"Alice", "Bob", "Charlie", "David", "Emily", "Frank", "Grace", "Heidi", "Ivan", "Judy", "Katie", "Laura", "Mallory", "Nancy", "Oscar", "Peggy", "Quentin", "Ralph", "Steve", "Trent", "Ursula", "Victor", "Wendy", "Xander", "Yvonne", "Zelda", "Adam", "Beth", "Cindy", "Derek", "Erica", "Fiona", "George", "Hannah", "Isaac", "Jackie", "Karen", "Lenny", "Mia", "Nate", "Olivia", "Pete", "Quinn", "Rachel", "Samantha", "Tina", "Vince", "Wade", "Ximena", "Yara"};

    // Function to generate a unique human-readable name for a QR code
    public static String generateQRCodeName(String hexString) {
        // Convert the hex string to a byte array
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);

        // Compute the SHA-256 hash of the byte array using Guava's Hashing.sha256()
        String hashString = Hashing.sha256().hashBytes(byteArray).toString();

        // Generate a random name
        StringBuilder nameBuilder = new StringBuilder();
        Random random = new Random(hashString.charAt(0));
        for (int i = 0; i < 3; i++) {
            int nameIndex = Math.abs(random.nextInt()) % nameArray.length;
            nameBuilder.append(nameArray[nameIndex]);
        }

        // Truncate the hash to the first three bytes
        String truncatedHashString = hashString.substring(0, 6);

        // Convert the truncated hash to an integer and take the absolute value
        int truncatedHashInt = Math.abs(Integer.parseInt(truncatedHashString, 16));

        // Generate a 6-digit number using the truncated hash integer
        truncatedHashInt = truncatedHashInt % 1000000;

        // Concatenate the random name and the 6-digit number to form the final QR code name
        return nameBuilder.toString() + String.format("%06d", truncatedHashInt);
    }
}
