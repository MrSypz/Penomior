package sypztep.penomior.common.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.data.BaseMobStatsEntry;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.LevelSystem;
import sypztep.penomior.common.util.XPDistributionUtil;

import java.util.Map;

public class XPDistributionEvent implements ServerLivingEntityEvents.AfterDeath {

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        //ตาย
        if (entity instanceof PlayerEntity player) {
            UniqueStatsComponent stats = ModEntityComponents.UNIQUESTATS.get(player);

            int lostXP = getNewXP(stats);
            Text message = Text.literal("You have lost: ")
                    .formatted(Formatting.GOLD)
                    .append(Text.literal(lostXP + " XP").formatted(Formatting.RED));
            if (ModConfig.lossxpnotify)
                player.sendMessage(message, false);
            LevelSystem levelSystem = stats.getLivingStats().getLevelSystem();
            levelSystem.subtractExperience(lostXP);
        }
        //ฆ่า
        if (damageSource.getSource() instanceof ServerPlayerEntity) {
            if (entity instanceof MobEntity living) {
                EntityType<?> mobEntityType = living.getType();
                for (EntityType<?> entityType : BaseMobStatsEntry.BASEMOBSTATS_MAP.keySet()) {
                    if (mobEntityType.equals(entityType)) {
                        BaseMobStatsEntry entry = BaseMobStatsEntry.BASEMOBSTATS_MAP.get(entityType);
                        if (entry == null)
                            continue;
                        distributeXP(entry);
                    }
                }
            }
            XPDistributionUtil.damageMap.clear();
        }
    }

    private static int getNewXP(UniqueStatsComponent stats) {
        return (int) Math.floor((stats.getXp() * ModConfig.xpLossPercentage));
    }
    private void distributeXP(BaseMobStatsEntry data) {
        int baseXP = data.exp();
        Map<ServerPlayerEntity, Integer> damageMap = XPDistributionUtil.damageMap;

        int totalDamage = damageMap.values().stream().mapToInt(Integer::intValue).sum();

        for (Map.Entry<ServerPlayerEntity, Integer> entry : damageMap.entrySet()) {
            ServerPlayerEntity player = entry.getKey();
            int damageDealt = entry.getValue();
            double proportion = (double) damageDealt / totalDamage;
            int xpAwarded = (int) (baseXP * proportion);

            UniqueStatsComponent stats = ModEntityComponents.UNIQUESTATS.get(player);
            stats.addExperience(xpAwarded);

            Text message = Text.literal("You have been awarded ")
                    .styled(style -> style.withColor(Formatting.AQUA))
                    .append(Text.literal(xpAwarded + " XP")
                            .styled(style -> style.withColor(Formatting.GOLD).withBold(true)))
                    .append(Text.literal(" for your contribution!")
                            .styled(style -> style.withColor(Formatting.AQUA)));
            if (ModConfig.xpnotify) {
                player.sendMessage(message, false);
            }
        }
    }
}
