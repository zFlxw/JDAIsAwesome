package com.github.zflxw.util;

import java.util.Random;

public class Utilities {
    public int generateRandomHexColor() {
        Random random = new Random();
        int randomNumber = random.nextInt(0xffffff + 1);

        return randomNumber;
    }
}
