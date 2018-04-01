package cn.nukkit.utils;

import java.util.Random;

public class Random {
    
    private static final Random random = new Random(System.currentTimeMillis());

    /**
     * Returns a random number between min (inkl.) and max (excl.) If you want a number between 1 and 4 (inkl) you need to call rand (1, 5)
     * 
     * @param min min inklusive value
     * @param max max exclusive value
     * @return
     */
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

    /**
     * Returns random boolean
     * @return  a boolean random value either <code>true</code> or <code>false</code>
     */
    public static boolean rand() {
        return random.nextBoolean();
    }

}
