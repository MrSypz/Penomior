package sypztep.penomior.common.util;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import sypztep.penomior.client.payload.AddTextParticlesPayload;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.stats.StatTypes;
import sypztep.penomior.common.tag.ModDamageTags;

import java.util.Random;

public final class CombatUtils {
    // Constants
    private static final int BASE_HIT = 175;
    private static final int BASE_FLEE = 100;
    private static final int MIN_HIT_RATE = 5;
    private static final int MAX_FLEE_RATE = 95;

    public static double calculateHitRate(double hitRate, double fleeRate) {
        return Math.max(hitRate - fleeRate, MIN_HIT_RATE);
    }

    public static double hitRate(UniqueStatsComponent attackerStats, UniqueStatsComponent targetStats, int totalAttackerAccuracy, int totalTargetEvasion) {
        int hitPoint = hit(attackerStats, totalAttackerAccuracy);
        double hitRate = 0;

        if (targetStats.getObj() instanceof PlayerEntity) {
            hitRate = hitPoint - totalTargetEvasion;
        } else if (targetStats.getObj() instanceof MobEntity) {
            int agi = targetStats.getLivingStats().getStat(StatTypes.AGILITY).getValue();
            int lvl = targetStats.getLevel();
            int hitRequire = 200 + lvl + agi;
            hitRate = 100 + hitPoint - hitRequire;
        }

        return MathHelper.clamp(hitRate, MIN_HIT_RATE, 100);
    }

    public static double fleeRate(UniqueStatsComponent attackerStats, UniqueStatsComponent targetStats, int totalTargetEvasion, int totalAttackerAccuracy, int attackers) {
        int fleePoint = flee(targetStats, totalTargetEvasion, attackers);
        double fleeRate = 0;

        if (targetStats.getObj() instanceof PlayerEntity) {
            int hitPoint = hit(attackerStats, totalAttackerAccuracy);
            fleeRate = 100 - (hitPoint - fleePoint);
        } else if (targetStats.getObj() instanceof MobEntity) {
            int dex = targetStats.getLivingStats().getStat(StatTypes.DEXTERITY).getValue();
            int lvl = targetStats.getLevel();
            int fleeRequire = 170 + lvl + dex;
            fleeRate = 95 + fleePoint - fleeRequire;
        }

        return MathHelper.clamp(fleeRate, 0, MAX_FLEE_RATE);
    }
    private static boolean isAttackHits(StatsComponent attackerStats, StatsComponent targetStats, UniqueStatsComponent attacker, UniqueStatsComponent target) {
        int attackerAccuracy = attackerStats.getAccuracy();
        int targetEvasion = targetStats.getEvasion();
        double hitRate = hitRate(attacker, target, attackerAccuracy, targetEvasion);
        double fleeRate = fleeRate(attacker, target, targetEvasion, attackerAccuracy, 1);

        double effectiveHitRate = calculateHitRate(hitRate, fleeRate);

        Random random = new Random();
        double chance = random.nextDouble() * 100;
        return chance < effectiveHitRate;
    }

    public static boolean isMissingHits(StatsComponent attackerStats, StatsComponent targetStats, UniqueStatsComponent attackerComponent, UniqueStatsComponent targetComponent) {
        return !isAttackHits(attackerStats, targetStats, attackerComponent, targetComponent);
    }

    private static int hit(UniqueStatsComponent levelComponent, int totalAccuracy) {
        int baseLvl = levelComponent.getLevel();
        return BASE_HIT + baseLvl + totalAccuracy;
    }

    private static int flee(UniqueStatsComponent levelComponent, int totalEvasion, int attackers) {
        int baseLvl = levelComponent.getLevel();
        return (int) (BASE_FLEE + (baseLvl + totalEvasion) * (1 - (attackers - 2) * 0.1f));
    }

    public static float damageModifier(LivingEntity target, float amount, DamageSource source) {
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
        return Math.max(value + value * pvpAttackMultiplier, 0);
    }

    private static float calculatePlayerVersEntity(LivingEntity target, float value) {
        float pveAttackMultiplier = (float) target.getAttributeValue(ModEntityAttributes.GENERIC_PLAYER_VERS_ENTITY_DAMAGE);
        return Math.max(value + value * pveAttackMultiplier, 0);
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
