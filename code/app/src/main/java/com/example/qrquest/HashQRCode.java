package com.example.qrquest;

import com.google.android.gms.common.util.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashQRCode {
//    String hexString = "4a616e652031393634";
//    byte[] byteArray = android.util.HexDump.hexStringToByteArray(hexString);
//    MessageDigest digest = MessageDigest.getInstance("SHA-256")
    public static void main(String[] args)
    {

        // The cursor will after GFG1
        // will at the start
        // of the next line
        System.out.println(get64LeastSignificantBitsForVersion1());

        // This will be printed at the
        // start of the next line
        System.out.println(get64LeastSignificantBitsForVersion1());
    }

    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong | variant3BitFlag;
    }

        public HashQRCode() throws NoSuchAlgorithmException {
        }
}
