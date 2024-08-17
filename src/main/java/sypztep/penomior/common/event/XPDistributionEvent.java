package sypztep.penomior.common.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.DamageTrackerUtil;

import java.util.Map;

public class XPDistributionEvent implements ServerLivingEntityEvents.AfterDeath {
    private final DamageTrackerUtil damageTracker = DamageHandlerEvent.damageTracker;

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        //Handle Death
        if (entity instanceof PlayerEntity player) {
            UniqueStatsComponent stats = ModEntityComponents.UNIQUESTATS.get(player);
            stats.getPlayerStats().getLevelSystem().subtractExperience(getNewXP(stats));
        }

        //Share EXP
        Map<ServerPlayerEntity, Integer> damageMap = damageTracker.getDamageMap();
        int totalDamage = damageTracker.getTotalDamage();

        for (Map.Entry<ServerPlayerEntity, Integer> entry : damageMap.entrySet()) {
            ServerPlayerEntity serverPlayer = entry.getKey();
            int playerDamage = entry.getValue();
            int xpToAward = (playerDamage / totalDamage) * 5; //TODO : 5 is the static value can change by xp value of the target
            ModEntityComponents.UNIQUESTATS.get(serverPlayer).addExperience(xpToAward);
        }
    }

    private static int getNewXP(UniqueStatsComponent stats) {
        int currentXP= stats.getXp();
        int maxXP= stats.getNextXpLevel();
        int totalExperience= currentXP + maxXP;
        // Calculate XP loss (5% of total XP)
        int xpLoss= (int) (totalExperience * 0.05f);
        return Math.max(0, xpLoss);
    }
}
