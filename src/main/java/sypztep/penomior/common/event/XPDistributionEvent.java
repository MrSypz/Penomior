package sypztep.penomior.common.event;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.init.ModEntityComponents;

public class XPDistributionEvent implements ServerLivingEntityEvents.AfterDeath {

    @Override
    public void afterDeath(LivingEntity entity, DamageSource damageSource) {
        UniqueStatsComponent stats;
        //Handle Death
        if (entity instanceof PlayerEntity player) {
            stats = ModEntityComponents.UNIQUESTATS.get(player);
            Text message = Text.literal("You have lose :")
                    .formatted(Formatting.GOLD)
                    .append(Text.literal(getNewXP(stats) + " XP").formatted(Formatting.RED));
            player.sendMessage(message, false);
            stats.getPlayerStats().getLevelSystem().subtractExperience(getNewXP(stats));
        }
        if (damageSource.getSource() instanceof ServerPlayerEntity player) {
            stats = ModEntityComponents.UNIQUESTATS.get(player);
            int xpRounded = 5; // XP to award for killing the target
            stats.addExperience(xpRounded);

            // Create and send a styled message to the player
            Text message = Text.literal("You have been awarded ")
                    .styled(style -> style.withColor(Formatting.AQUA))
                    .append(Text.literal(xpRounded + " XP")
                            .styled(style -> style.withColor(Formatting.GOLD).withBold(true)))
                    .append(Text.literal(" for your contribution!")
                            .styled(style -> style.withColor(Formatting.AQUA)));

            player.sendMessage(message, false);
        }
    }

    private static int getNewXP(UniqueStatsComponent stats) {
        int currentXP = stats.getXp();
        int maxXP = stats.getNextXpLevel();
        int totalExperience = currentXP + maxXP;
        int xpLoss = (int) (totalExperience * 0.05f);
        return Math.max(0, xpLoss);
    }
}
