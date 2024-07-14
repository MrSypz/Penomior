package sypztep.penomior.common.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import sypztep.penomior.common.api.iframe.EntityKnockbackCallback;
import sypztep.penomior.common.api.iframe.PlayerEntityAccessor;

public class KnockBackThresoEvent implements EntityKnockbackCallback {
    @Override
    public ActionResult takeKnockback(LivingEntity entity, Entity source, double strength, double x, double z) {
        if (entity.getEntityWorld().isClient) {
            return ActionResult.PASS;
        }
        if (source != null) {
            if (source instanceof PlayerEntity player) {
                PlayerEntityAccessor playerAccessor = (PlayerEntityAccessor) player;
                if (playerAccessor.isSwingingHand()) {
                    playerAccessor.setSwingingHand(false);
                    return ActionResult.FAIL;
                }
            }
        }
        return ActionResult.PASS;
    }
}
