package sypztep.penomior.common.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import sypztep.penomior.common.component.StatsComponent;

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

    public static float newDamageLeftCalculate(LivingEntity livingEntity, float damageAmount, DamageSource damageSource, float armor, float armorToughness) {
        float finalFactor;
        World world;

        float finalArmorToughness = 2.0f + armorToughness / 4.0f;
        float armorValue = armor + finalArmorToughness;
        float damageReduction = Math.min(armorValue / (armorValue + 100), 0.9f);

        ItemStack itemStack = damageSource.getWeaponStack();
        if (itemStack != null && (world = livingEntity.getWorld()) instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            finalFactor = MathHelper.clamp(EnchantmentHelper.getArmorEffectiveness(serverWorld, itemStack, livingEntity, damageSource, armorValue), 0.0f, 1.0f);
        } else
            finalFactor = damageReduction;

        return damageAmount * (1.0f - finalFactor);
    }

}
