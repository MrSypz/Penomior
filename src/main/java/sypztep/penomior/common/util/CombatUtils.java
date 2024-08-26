package sypztep.penomior.common.util;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import sypztep.penomior.Penomior;
import sypztep.penomior.client.payload.AddTextParticlesPayload;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.tag.ModDamageTags;

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

    public static float newDamageLeft(LivingEntity livingEntity, float damageAmount, DamageSource damageSource, float armor, float armorToughness) {
        World world;
        ItemStack itemStack = damageSource.getWeaponStack();
        float effectiveArmor;
        float armorPenetration;  // Default to no armor penetration
        float delta = 0.0f;  // Initialize delta to track armor reduction

        // Step 1: Calculate armor penetration if the weapon stack is present and in a server world
        if (itemStack != null && (world = livingEntity.getWorld()) instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            armorPenetration = EnchantmentHelper.getArmorEffectiveness(serverWorld, itemStack, livingEntity, damageSource, armor);
            delta = armor - armorPenetration;  // Calculate the amount of armor reduced by penetration
            Penomior.LOGGER.info("Armor Penetration: {} ({}% reduction)", delta, delta * 100);
        }

        // Step 2: Calculate effective armor after applying armor penetration (reduce by delta)
        effectiveArmor = armor - delta;
        Penomior.LOGGER.info("Original Armor: {}", armor);
        Penomior.LOGGER.info("Delta (Armor Reduction): {}", delta);
        Penomior.LOGGER.info("Effective Armor after Penetration: {}", effectiveArmor);

        // Step 3: Calculate damage reduction based on effective armor
        float armorReduction = effectiveArmor / (effectiveArmor + 100); // Example formula for armor reduction scaling
        float damageAfterArmor = damageAmount * (1 - armorReduction);
        Penomior.LOGGER.info("Damage after Armor Reduction: {}", damageAfterArmor);
        Penomior.LOGGER.info("Armor Reduction Percentage: {}%", armorReduction * 100);

        // Step 4: Apply flat damage reduction (armorToughness / 2 as flat DR)
        float flatDamageReduction = armorToughness * 0.01f;
        float damageAfterFlatDR = damageAfterArmor - flatDamageReduction;
        Penomior.LOGGER.info("Flat Damage Reduction: {}", flatDamageReduction);

        // Ensure damage doesn't go below zero after flat DR
        damageAfterFlatDR = Math.max(damageAfterFlatDR, 0);
        Penomior.LOGGER.info("Final Damage after Flat DR: {}", damageAfterFlatDR);

        // Return the final damage
        return damageAfterFlatDR;
    }



    public static float damageModifier(LivingEntity target, float amount, DamageSource source) {
        // Determine if the damage is of a specific type and apply appropriate modifiers
        LivingEntity attacker = (LivingEntity) source.getAttacker();
        if (attacker != null) {
            if (source.isIn(ModDamageTags.MAGIC_DAMAGE)) {
                amount = calculateMagicDamage(attacker, amount);
            }
            if (source.isIn(ModDamageTags.PROJECTILE_DAMAGE)) {
                amount = calculateProjectileDamage(attacker, amount);
            }
            if (source.isIn(ModDamageTags.MELEE_DAMAGE)) {// not empty hand
                amount = calculateMeleeDamage(attacker, amount);
            }
            if (source.isIn(ModDamageTags.PHYSICAL_DAMAGE)) {
                amount = calculatePhysicalDamage(attacker, amount);
            }
            amount = calculatePlayerVersPlayer(attacker, amount);
            amount = calculatePlayerVersEntity(attacker, amount);
        }
        if (source.isIn(ModDamageTags.PROJECTILE_DAMAGE))
            return amount;

        // Apply back attack multiplier
        return applyBackAttackModifier(target, source, amount);
    }

    private static float calculateMagicDamage(LivingEntity target, float value) {
        float magicAttackMultiplier = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_MAGIC_ATTACK_DAMAGE);
        float magicResistance = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_MAGIC_RESISTANCE);
        float amplifiedDamage = value + value * magicAttackMultiplier;
        float finalDamage = amplifiedDamage - magicResistance;
        return Math.max(finalDamage, 0);
    }

    private static float calculateProjectileDamage(LivingEntity target, float value) {
        float projectileAttackMultiplier = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_PROJECTILE_ATTACK_DAMAGE);
        float projectileResistance = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_PROJECTILE_RESISTANCE);
        float amplifiedDamage = value + value * projectileAttackMultiplier;
        float finalDamage = amplifiedDamage - projectileResistance;
        return Math.max(finalDamage, 0);
    }
    private static float calculatePlayerVersPlayer(LivingEntity target, float value) {
        float pvpAttackMultiplier = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_PLAYER_VERS_PLAYER_DAMAGE);
        return Math.max(value + value * pvpAttackMultiplier,0);
    }
    private static float calculatePlayerVersEntity(LivingEntity target, float value) {
        float pveAttackMultiplier = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_PLAYER_VERS_ENTITY_DAMAGE);
        return Math.max(value + value * pveAttackMultiplier,0);
    }

    private static float calculateMeleeDamage(LivingEntity target, float value) {
        float meleeAttackMultiplier = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_MELEE_ATTACK_DAMAGE);
        float amplifiedDamage = value + value * meleeAttackMultiplier;
        return Math.max(amplifiedDamage, 0);
    }

    private static float calculatePhysicalDamage(LivingEntity target, float value) {
        float physicalResistance = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_PHYSICAL_RESISTANCE);
        return Math.max(value - value * physicalResistance, 0);
    }

    private static float applyBackAttackModifier(LivingEntity target, DamageSource source, float value) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingAttacker) {
            float angleDifference = Math.abs(MathHelper.subtractAngles(target.getHeadYaw(), source.getSource().getHeadYaw()));
            if (angleDifference <= 75) {
                if (livingAttacker instanceof PlayerEntity) {
                    PlayerLookup.tracking((ServerWorld) target.getWorld(), target.getChunkPos()).forEach(foundPlayer ->
                            AddTextParticlesPayload.send(foundPlayer, target.getId(), AddTextParticlesPayload.TextParticle.BACKATTACK)
                    );
                    PlayerLookup.tracking((ServerWorld) livingAttacker.getWorld(), livingAttacker.getChunkPos()).forEach(foundPlayer ->
                            AddTextParticlesPayload.send(foundPlayer, target.getId(), AddTextParticlesPayload.TextParticle.BACKATTACK)
                    );
                }
                return value * 1.5F;
            }
        }
        return value; // Return unmodified value if not a back attack
    }

    public static double getCritChance(LivingEntity living) {
        return living.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_CHANCE);
    }

    public static boolean doCrit(LivingEntity living) {
        return living.getRandom().nextFloat() < getCritChance(living);
    }

    public static void applyParticle(Entity target) {
        if (target != null) {
            PlayerLookup.tracking((ServerWorld) target.getWorld(), target.getChunkPos())
                    .forEach(foundPlayer -> AddTextParticlesPayload.send(
                            foundPlayer, target.getId(),
                            AddTextParticlesPayload.TextParticle.CRITICAL
                    ));
        }
    }
}
