package sypztep.penomior.common.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import sypztep.penomior.common.component.StatsComponent;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public final class CombatUtils {
    // Constants
    private static final double ACCURACY_COEFFICIENT = 0.2624;
    private static final double EVASION_COEFFICIENT = -0.2101;
    private static final double BASE_HIT_RATE = 60.713;

    public static double calculateHitRate(int totalAccuracy, int totalEvasion) {
        return ACCURACY_COEFFICIENT * totalAccuracy + EVASION_COEFFICIENT * totalEvasion + BASE_HIT_RATE;
    }

    public static boolean isAttackHits(StatsComponent attackerStats, StatsComponent targetStats) {
        int attackerAccuracy = attackerStats.getAccuracy();
        int targetEvasion = targetStats.getEvasion();
        double hitrate = CombatUtils.calculateHitRate(attackerAccuracy, targetEvasion);
        Random random = new Random();
        return random.nextDouble() < hitrate * 0.01f;
    }

    public static boolean isMissingHits(StatsComponent attackerStats, StatsComponent targetStats) {
        return !isAttackHits(attackerStats, targetStats);
    }

    public static float newDamageLeftCalculate(LivingEntity armorWearer, float damageAmount, DamageSource damageSource, float armor, float armorToughness) {
        float i;
        World world;
        float finalDamageReductionFactor = getFinalDamageReductionFactor(armor, armorToughness);
        float f = 2.0f + armorToughness / 4.0f;
        float g = MathHelper.clamp(armor - damageAmount / f, armor * 0.2f, 20.0f);
        float h = g / 25.0f;
        ItemStack itemStack = damageSource.getWeaponStack();
        if (itemStack != null && (world = armorWearer.getWorld()) instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            i = MathHelper.clamp(EnchantmentHelper.getArmorEffectiveness(serverWorld, itemStack, armorWearer, damageSource, h), 0.0f, 1.0f);
        } else {
            i = h;
        }
        float j = 1.0f - i;

        return damageAmount * finalDamageReductionFactor * j;
    }

    private static float getFinalDamageReductionFactor(float armor, float armorToughness) {
        float totalPoints = armor + armorToughness * 3;
        Map<Integer, Float> damageReductionMap = getIntegerFloatMap();

        // Calculate damage reduction percentage based on total points
        float damageReductionPercentage = 0.0f;
        for (Map.Entry<Integer, Float> entry : damageReductionMap.entrySet()) {
            if (totalPoints <= entry.getKey()) {
                damageReductionPercentage = entry.getValue();
                break;
            }
        }
        return 1.0f - damageReductionPercentage;
    }

    private static @NotNull Map<Integer, Float> getIntegerFloatMap() {
        Map<Integer, Float> damageReductionMap = new TreeMap<>();
        damageReductionMap.put(3, 0.01f);
        damageReductionMap.put(7, 0.02f);
        damageReductionMap.put(11, 0.03f);
        damageReductionMap.put(15, 0.04f);
        damageReductionMap.put(19, 0.05f);
        damageReductionMap.put(23, 0.06f);
        damageReductionMap.put(27, 0.07f);
        damageReductionMap.put(31, 0.08f);
        damageReductionMap.put(35, 0.09f);
        damageReductionMap.put(39, 0.10f);
        damageReductionMap.put(43, 0.11f);
        damageReductionMap.put(47, 0.12f);
        damageReductionMap.put(51, 0.13f);
        damageReductionMap.put(55, 0.14f);
        damageReductionMap.put(59, 0.15f);
        damageReductionMap.put(63, 0.16f);
        damageReductionMap.put(67, 0.17f);
        damageReductionMap.put(71, 0.18f);
        damageReductionMap.put(75, 0.19f);
        damageReductionMap.put(79, 0.20f);
        damageReductionMap.put(83, 0.21f);
        damageReductionMap.put(87, 0.22f);
        damageReductionMap.put(91, 0.23f);
        damageReductionMap.put(95, 0.24f);
        damageReductionMap.put(99, 0.25f);
        damageReductionMap.put(103, 0.26f);
        damageReductionMap.put(107, 0.27f);
        damageReductionMap.put(111, 0.28f);
        damageReductionMap.put(115, 0.29f);
        damageReductionMap.put(Integer.MAX_VALUE, 0.30f); // Any value above 115 points
        return damageReductionMap;
    }
}
