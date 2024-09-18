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
import sypztep.penomior.common.data.MobStatsEntry;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.XPDistributionUtil;

import java.util.Map;

public class XPDistributionEvent implements ServerLivingEntityEvents.AfterDeath {

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        UniqueStatsComponent stats;
        //ตาย
        if (entity instanceof PlayerEntity player) {
            stats = ModEntityComponents.UNIQUESTATS.get(player);
            Text message = Text.literal("You have lose :")
                    .formatted(Formatting.GOLD)
                    .append(Text.literal(getNewXP(stats) + " XP").formatted(Formatting.RED));
            if (ModConfig.lossxpnotify)
                player.sendMessage(message, false);
            stats.getLivingStats().getLevelSystem().subtractExperience(getNewXP(stats));
        }
        //ฆ่า
        if (damageSource.getSource() instanceof ServerPlayerEntity) {
            if (entity instanceof MobEntity living) {
                EntityType<?> mobEntityType = living.getType();
                for (EntityType<?> entityType : MobStatsEntry.MOBSTATS_MAP.keySet()) {
                    if (mobEntityType.equals(entityType)) {
                        MobStatsEntry entry = MobStatsEntry.MOBSTATS_MAP.get(entityType);
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
        int currentXP = stats.getXp();
        int maxXP = stats.getNextXpLevel();
        int totalExperience = currentXP + maxXP;
        int xpLoss = (int) (totalExperience * 0.05f);
        return Math.max(0, xpLoss);
    }
    private void distributeXP(MobStatsEntry data) {
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
