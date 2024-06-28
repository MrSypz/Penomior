package sypztep.penomior.common.util;

public class CombatUtils {
    // Base rates
    private static final double BASE_HIT_RATE = 0.60;
    private static final double BASE_EVASION_RATE = 0.40;

    // Maximum rates
    private static final double MAX_HIT_RATE = 1.00;
    private static final double MAX_EVASION_RATE = 0.90;

    // Conversion factors
    private static final double ACCURACY_TO_HIT_RATE = 0.01 / 4.0;
    private static final double EVASION_TO_EVASION_RATE = 0.01 / 5.0;

    public static double calculateHitRate(int accuracy) {
        double hitRate = BASE_HIT_RATE + accuracy * ACCURACY_TO_HIT_RATE;
        return Math.min(hitRate, MAX_HIT_RATE); // Clamp to maximum hit rate
    }

    public static double calculateEvasionRate(int evasion) {
        double evasionRate = BASE_EVASION_RATE + evasion * EVASION_TO_EVASION_RATE;
        return Math.min(evasionRate, MAX_EVASION_RATE); // Clamp to maximum evasion rate
    }
}
