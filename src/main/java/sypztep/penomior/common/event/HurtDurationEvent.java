package sypztep.penomior.common.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import sypztep.penomior.ModConfig;
import sypztep.penomior.common.api.iframe.EntityHurtCallback;

public class HurtDurationEvent implements EntityHurtCallback {
    @Override
    public ActionResult hurtEntity(LivingEntity entity, DamageSource source, float amount) {
        if (entity.getEntityWorld().isClient) {
            return ActionResult.PASS;
        }
        Identifier identifier = EntityType.getId(entity.getType());
        for (String id : ModConfig.attackExcludedEntities) {
            Entity attacker = source.getAttacker();
            if (attacker == null)
                break;
            if (identifier == null)
                break;
            int starIndex = id.indexOf('*');
            if (starIndex != -1) {
                if (identifier.toString().contains(id.substring(0, starIndex)))
                    return ActionResult.PASS;
            } else if (identifier.toString().equals(id))
                return ActionResult.PASS;
        }
        if (source.getSource() instanceof PlayerEntity || source.getSource() instanceof LivingEntity)
            entity.timeUntilRegen = ModConfig.iFrameDuration; // 1.8 combat iframe 0.5 ticks

        return ActionResult.PASS;
    }
}
