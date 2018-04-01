package cn.nukkit.entity;

import java.util.Random;

public class Utils {

    private static final Random random = new Random(System.currentTimeMillis());

    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return min + random.nextInt(max - min);
    }

    public static double rand(double min, double max){
        if(min == max){
            return max;
        }
        return min + Math.random() * (max-min);
    }

    public static boolean rand() {
        return random.nextBoolean();
    }

}
