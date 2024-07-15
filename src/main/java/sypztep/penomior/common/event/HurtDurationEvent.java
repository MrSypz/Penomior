package sypztep.penomior.common.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import sypztep.penomior.ModConfig;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.api.iframe.EntityHurtCallback;

public class HurtDurationEvent implements EntityHurtCallback {
    @Override
    public ActionResult hurtEntity(LivingEntity entity, DamageSource source, float amount) {
        if (entity.getEntityWorld().isClient)
            return ActionResult.PASS;

        Entity attacker = source.getAttacker();
        if (attacker == null || isExcludedEntity(attacker, ModConfig.dmgReceiveExcludedEntities))
            return ActionResult.PASS;

        if (isExcludedEntity(entity, ModConfig.attackExcludedEntities))
            return ActionResult.PASS;

        entity.timeUntilRegen = ModConfig.iFrameDuration; // 1.8 combat iframe 0.5 ticks
        return ActionResult.PASS;
    }


    private boolean isExcludedEntity(Entity entity, Iterable<String> excludedEntities) {
        Identifier identifier = EntityType.getId(entity.getType());
        if (identifier == null) {
            return false;
        }

        String idString = identifier.toString();
        for (String id : excludedEntities) {
            if (id.contains("*")) {
                if (idString.contains(id.replace("*", ""))) {
                    return true;
                }
            } else if (idString.equals(id)) {
                return true;
            }
        }

        return false;
    }
}