package com.example.qrquest;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qrquest.databinding.FragmentQrDetectedBinding;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;

/**
 * This class represents the QR Detected Screen. It also does hashing for a name, score, and a visual
 * representation from the raw value scanned from the QR code.
 * @author Zijing Lu
 * @author Jay Pasrija
 * @author Michael Wolowyk
 * @author Thea Nguyen
 */
public class QRDetectedFragment extends Fragment {

    FragmentQrDetectedBinding binding;
    String rawValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQrDetectedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // get the raw value of the QR code
        Bundle bundle = getArguments();
        assert bundle != null;
        rawValue = bundle.getString("rawValue");

        // display the hashed name
        String qrName = hashName(rawValue);
        binding.nameTextDisplay.setText(qrName);

        // display the hashed score
        int qrScore = hashScore(rawValue);
        binding.scoreTextDisplay.setText(String.valueOf(qrScore));

        return view;
    }


    // hash the name
    public String hashName(@NonNull String hexString) {
        String[] adv = {"overly", "nicely", "evely", "only", "girly", "manly", "extremely", "hardly",
                        "sadly", "nearly", "officially", "gently", "literally", "fairly", "merely",
                        "really", "not", "merely", "lively", "truly", "merrily", "potentially",
                        "dangerously", "suspiciously"};

        String[] adj = {"Raspy", "Mean", "Old", "Sleepy", "Tired", "Pure", "Poetic", "Shiny", "Skinny",
                "Super", "Witty", "Stiff", "Rare", "Crazy", "Dainty", "Lucky", "Common", "Wiry", "Absurd",
                "Gloomy", "Vague", "Boorish","Shy", "Exhausted", "Tidy", "Smelly", "Sparkling", "Abnormal",
                "Harsh", "Tacky", "Unusual", "Arrogant", "Unkempt", "Prickly", "Grumpy", "Innate",
                "Cautious", "Clear", "Gifted", "Calm", "Icy", "Hurried", "Sus", "Mad", "Cute",
                "Royal", "Troubled", "Silly", "Gooey", "Greasy"};

        String[] name = {"Akali", "Ahri", "Amumu", "Ashe", "Bard", "Brand", "Caitlyn", "Cassiopeia",
                "Darius", "Draven", "Elise", "Ezreal", "Fiora", "Fizz", "Galio", "Garen", "Irelia",
                "Janna", "Jinx", "Katarina", "Leona", "Lucian", "Lulu", "Lux", "Morgana", "Nunu",
                "Adam", "Olaf", "Pobby", "Quinn", "Riven", "Ryze", "Shyvana", "Sion", "Sivir",
                "Sona", "Soraka", "Lenny", "Teemo", "Thresh", "Tristana", "Urgot", "Varus", "Vayne",
                "Wukong", "Vi", "Yasuo", "Zac", "Zed", "Zyra"};

        // convert the hex string to a byte array
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);

        // compute SHA-256 hash of the byte array
        String hashString = Hashing.sha256().hashBytes(byteArray).toString();

        // generate a random name
        StringBuilder nameBuilder = new StringBuilder();
        Random random = new Random(hashString.charAt(0));
        for (int i = 0; i < 3; i++) {
            int index;
            if (i == 0) {
                index = Math.abs(random.nextInt()) % adv.length;
                nameBuilder.append(adv[index]);
            } else if (i == 1) {
                index = Math.abs(random.nextInt()) % adj.length;
                nameBuilder.append(adj[index]);
            } else {
                index = Math.abs(random.nextInt()) % name.length;
                nameBuilder.append(name[index]);
            }
        }

        // truncate the hash to the first three bytes
        String truncatedHashString = hashString.substring(0, 6);

        // convert the truncated hash to an integer and take the absolute value
        int truncatedHashInt = Math.abs(Integer.parseInt(truncatedHashString, 16));

        // generate a 6-digit number from the truncated hash integer
        truncatedHashInt %= 1000000;

        return nameBuilder + String.format(Locale.CANADA, "%06d", truncatedHashInt);
    }

    // hash the score
    public int hashScore(@NonNull String hexString) {
        byte[] byteArray = hexString.getBytes(StandardCharsets.UTF_8);
        int score = 0;
        for (byte b : byteArray) {
            score += b;
        }
        return score;
    }
}
