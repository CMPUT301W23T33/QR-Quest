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
 * @author Thea Nguyen
 */
public class Utilities {

    /**
     * This method hashes a name from the raw value of the qr code.
     * @param hexString a string
     * @return a hashed string
     */
    // hash the name
    public String hashName(@NonNull String hexString) {

        String[] adj = {"Last", "Lush", "Dry", "Mere", "Bad", "Big", "Cute", "Fair", "True", "Odd",
                "Nice", "Wet", "Lame", "Nine", "Good", "Wiry", "Soft", "Free", "Busy", "Huge", "Gray",
                "Glib", "Mute", "Like", "Bent", "Damp", "Zany", "Flat", "Thin", "Four", "Wary", "Sore",
                "Oval", "Rare", "Cold", "Rich", "Ripe", "Ajar", "Past", "Dear", "New", "Able", "Gamy",
                "Left", "Mad", "Pink", "Red", "Next", "Neat","Slim"};

        String[] name = {"Army", "Oven", "Wife", "User", "Tale", "Food", "Song", "Meat", "Bird", "Fact",
                "Beer", "Meal", "Goal", "Debt", "Year", "Girl", "Week", "Exam", "Lady", "Soup","Data",
                "Cell", "Poem", "Area", "News", "Loss", "Bath", "Math", "City", "Role", "Road", "Hall",
                "Dirt", "Wood", "Gene", "King", "Mode", "Gate", "Hair", "Love", "Menu", "Lake", "Mood",
                "Idea", "Unit", "Mall", "Town", "Disk", "Desk", "Poet"};

        // convert the hex string to a byte array
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);

        // compute SHA-256 hash of the byte array
        String hashString = Hashing.sha256().hashBytes(byteArray).toString();

        // generate a random name
        StringBuilder nameBuilder = new StringBuilder();
        Random random = new Random(hashString.charAt(0));
        for (int i = 0; i < 3; i++) {
            int index = Math.abs(random.nextInt()) % adj.length;
            if (i == 0 || i == 1)
                nameBuilder.append(adj[index]);
            else
                nameBuilder.append(name[index]);
        }

        // truncate the hash to the first three bytes
        String truncatedHashString = hashString.substring(0, 6);

        // convert the truncated hash to an integer and take the absolute value
        int truncatedHashInt = Math.abs(Integer.parseInt(truncatedHashString, 16));

        // generate a 6-digit number from the truncated hash integer
        truncatedHashInt %= 1000000;

        return nameBuilder.toString() + truncatedHashInt;
    }

    /**
     * This method hashes a score from the raw value of the qr code
     * @param hexString a string
     * @return a hashed integer
     */
    public int hashScore(@NonNull String hexString) {
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);
        int score = 0;
        for (byte b : byteArray)
            score += b;
        return score;
    }
}
