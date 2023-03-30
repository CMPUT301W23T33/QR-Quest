package com.example.qrquest;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.common.hash.Hashing;

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

    // Dictionary of characters to choose from for in the creation of visual representation (testing for visual rep)
    // First bit: left arm (\ vs /), second bit face: () vs [] (need an if condition to make sure face matches), third bit: left eye (X or O)
    // fourth bit: mouth (v or _), fifth bit: right eye (o or x) and sixth bit: right hand (\ or /)
    private static char[][] faceOptions = {
            {'\\','/'},
            {'[','('},
            {'O','X'},
            {'v','_'},
            {'o','x'},
            {'/','\\'}
    };

    /**
     *
     * @param hexString
     * @return
     */
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

    /**
     *
     * @param hexString
     * @return
     */
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
     *
     * @param hexString
     * @return
     */
    public static Uri visualRepresentation(@NonNull String hexString) {
        // Setup bitmap
        Bitmap image = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888); // need width and height
        Canvas faceCanvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);

        // First bit: left arm (\ vs /), second bit face: () vs [] (need an if condition to make sure face matches), third bit: left eye (X or O)
        // fourth bit: mouth (v or _), fifth bit: right eye (o or x) and sixth bit: right hand (\ or /)

        // Convert the hex string to a byte array
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);

        // Compute the SHA-256 hash of the byte array using Guava's Hashing.sha256()
        String hashString = Hashing.sha256().hashBytes(byteArray).toString();

        // Using the first 6 bits of the hash, modify the copy of the defaultFace to create a unique visual representation
        StringBuilder imgBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
        // have an if condition that checks the index position of the left side of face to make sure it matches the right side of the face (when going onto the sixth item)
            int dictionaryIndex = i % faceOptions.length;
            char[] dictionary = faceOptions[dictionaryIndex];
            int wordIndex = (hashString.charAt(i) & 0x01) % dictionary.length;
            if (i == 5) {
                if ((imgBuilder.charAt(1)) == '(') {
                    imgBuilder.append(')');
                }
                else {
                    imgBuilder.append(']');
                }
            }
            imgBuilder.append(dictionary[wordIndex]);
        // have an if condition that checks the index position of the left side of face to make sure it matches the right side of the face
        }
        // Put the created string face onto the Bitmap canvas
        faceCanvas.drawText(imgBuilder.toString(), 50, 50, paint);
        // convert bitmap to Uri
        Uri imageUri = Uri.parse(image.toString()); // or String.valueOf()?

        // return Bitmap image
        //return image;

        // return Uri
        return imageUri;
    }
}
