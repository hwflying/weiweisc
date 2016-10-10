package com.weiweisc.util;

import java.util.Random;

public class GenerateString {

    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for(int i = 0; i < length; i++){
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; 
            if ("char".equalsIgnoreCase(charOrNum)) { 
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; 
                val += (char) (choice + random.nextInt(26));
            }
            else if ("num".equalsIgnoreCase(charOrNum)) { 
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    public static String generateKey() {
        return getCharAndNumr(32).toUpperCase();
    }
}
