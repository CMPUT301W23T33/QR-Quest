package com.example.qrquest;


import android.content.Context;

import androidx.annotation.NonNull;

import com.google.common.hash.Hashing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import kotlin.text.Charsets;

/**
 * This class consists of different functions that are utilised in the project.
 * @author Zijing Lu
 * @author Jay Pasrija
 * @author Michael Wolowyk
 * @author Thea Nguyen
 * */
public class Utilities {

    private static String[] adverbs, adjectives, nouns;

    /**
     * This method reads the word.json file that contains three lists of adverbs, adjectives, nouns
     * @param context the context of the activity
     * @param fileName the filename (word.json)
     * @throws IOException is thrown if the file is not opened successfully
     * @throws JSONException is thrown if the JSON object is not created successfully
     */
    public static void readJSON(@NonNull Context context, String fileName) throws IOException, JSONException {
        InputStream inputStream;
        int size, readBytes;
        byte[] buffer;

        // Open the file from the assets folder and read the json to a byte array
        inputStream = context.getAssets().open(fileName);
        size = inputStream.available();
        buffer = new byte[size];
        readBytes = inputStream.read(buffer);
        inputStream.close();

        // Convert the file into json
        if (readBytes == size) {
            String json = new String(buffer, Charsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            String[] keys = {"adverbs", "adjectives", "nouns"};
            for (int index = 0; index < keys.length; ++index) {
                JSONArray array = jsonObject.getJSONArray(keys[index]);
                ArrayList<String> stringArrayList = new ArrayList<>(array.length());
                for (int i = 0; i < array.length(); ++i) {
                    stringArrayList.add(array.getString(i));
                }
                if (index == 0) {
                    adverbs = new String[array.length()];
                    stringArrayList.toArray(adverbs);
                }
                else if (index == 1) {
                    adjectives = new String[array.length()];
                    stringArrayList.toArray(adjectives);
                }
                else {
                    nouns = new String[array.length()];
                    stringArrayList.toArray(nouns);
                }
            }
        }

    }

    /**
     * This method hashes an integer from the given string
     * @param hexString a string that needs to be hashed
     * @return a hashed integer
     */
    public static int hashScore(@NonNull String hexString) {
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);
        int score = 0;
        for (byte b : byteArray) {
            score += b;
        }
        return score;
    }

    /**
     * This method hashes a human-readable name from the given string
     * @param hexString a string that needs to be hashed
     * @return a hashed and readable name
     */
    @NonNull
    public static String hashName(@NonNull String hexString) {
        // Convert the hex string into 256 hashed bits string
        final String hashString = Hashing.sha256()
                .hashString(hexString, StandardCharsets.UTF_8)
                .toString();

        // divide 32 bytes into 4 parts, each part has 8 bytes
        long currentNumber = 0;
        int currentArrays = 0;
        StringBuilder name = new StringBuilder();
        String[][] arrays = {adverbs, adjectives, nouns};
        for (int i = 0; i < hashString.length(); ++i) {
            currentNumber = currentNumber * 256 + Long.decode("0x" + (hashString.charAt(i)));
            if (i == 7 || i == 15 ||  i == 23) {
                int remainder = (int) currentNumber % arrays[currentArrays].length;
                name.append(arrays[currentArrays][remainder]);
                currentArrays += 1;
                currentNumber = 0;
            }
        }
        return name.toString();
    }
}
