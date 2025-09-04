package com.boc.voucherjavademo.tools;

import java.util.Random;

public class TellerUtils {
    private static final Random RANDOM = new Random();
    public static String generateTellerNo(){
        int randomNumber = RANDOM.nextInt(10_000_000);
        return String.format("%07d", randomNumber);
    }
}
