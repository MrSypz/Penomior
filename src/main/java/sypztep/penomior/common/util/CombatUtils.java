package sypztep.penomior.common.util;

public class CombatUtils {
    // Constants
    private static final double ACCURACY_COEFFICIENT = 0.2624;
    private static final double EVASION_COEFFICIENT = -0.2101;
    private static final double BASE_HIT_RATE = 60.713;

    public static double calculateHitRate(int totalAccuracy, int totalEvasion) {
        return ACCURACY_COEFFICIENT * totalAccuracy + EVASION_COEFFICIENT * totalEvasion + BASE_HIT_RATE;
    }
}
