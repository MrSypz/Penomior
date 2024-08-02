package sypztep.penomior.common.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.data.DamageReductionEntry;

import java.util.Map;
import java.util.Random;

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
        float finalDamageReductionFactor = 1 - getFinalDamageReductionFactor(armor, armorToughness);
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

    public static float getFinalDamageReductionFactor(float armor, float armorToughness) {
        float totalPoints = armor + armorToughness * 3;
        Map<Integer, Float> damageReductionMap = DamageReductionEntry.DAMAGEREDUCTION_ENTRY_MAP;
        // Calculate damage reduction percentage based on total points
        float damageReductionPercentage = 0.0f;
        for (Map.Entry<Integer, Float> entry : damageReductionMap.entrySet()) {
            if (totalPoints <= entry.getKey()) {
                damageReductionPercentage = entry.getValue();
                break;
            }
        }
        return damageReductionPercentage;
    }
}
