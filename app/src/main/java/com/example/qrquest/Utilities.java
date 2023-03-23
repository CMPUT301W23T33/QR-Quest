package com.example.qrquest;


import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.common.hash.Hashing;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * This class consists of different functions that are utilised in the project.
 * @author Zijing Lu
 * @author Jay Pasrija
 * @author Michael Wolowyk
 * @author Abinand Nanthananthan
 * */
public class Utilities {

    // Dictionaries of words to choose from
    private static final String[][] DICTIONARIES = {
            {"Freezing", "Magma"},
            {"Spirit", "Spark"},
            {"ing", "ling"},
            {"Tiny", "Huge"},
            {"Common", "Legendary"},
            {"Monster", "Beast"}
    };

    // Dictionary of characters to choose from for in the creation of visual representation
    private static final String[][] characterDictionary = {};

    // Function to generate a unique human-readable name for a QR code
    @NonNull
    public static String hashName(@NonNull String hexString) {
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

        String truncatedHashString = hashString.substring(6, 12);
        int truncatedHashInt = Math.abs(Integer.parseInt(truncatedHashString, 16));
        truncatedHashInt %= 10000;

        return nameBuilder + String.format(Locale.CANADA, "%06d", truncatedHashInt);
    }

    // Function to generate a score based on the hash of the qr code
    public static int hashScore(@NonNull String hexString) {
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);
        int score = 0;
        for (byte b : byteArray) {
            score += b;
        }
        return score;
    }

    /**
     * Function that creates a visual representation of the QR Hash using ASCII characters,
     * and using Bitmaps, returns it as an image
     * @param hexString
     * @return
     *
     */
    public static Bitmap visualRepresentation(@NonNull String hexString) {
        Bitmap image = Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888); // need width and height
        // Convert the hex string to a byte array
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);

        // Compute the SHA-256 hash of the byte array using Guava's Hashing.sha256()
        String hashString = Hashing.sha256().hashBytes(byteArray).toString();



        // return image or set the view to the bitmap?
        // viewpager2.setBitMap(image) ?
        //return image;
    }
}
