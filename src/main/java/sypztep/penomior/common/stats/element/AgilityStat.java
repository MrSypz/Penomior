package sypztep.penomior.common.stats.element;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.Stat;

public class AgilityStat extends Stat {
    public AgilityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(ServerPlayerEntity player) {
        applyEffect(player, EntityAttributes.GENERIC_ATTACK_SPEED, getPrimaryId(), EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> (0.01 * this.currentValue));
    }

    @Override
    public void applySecondaryEffect(ServerPlayerEntity  player) {
        var evasion = ModEntityComponents.STATS.get(player);
        evasion.addExtraEvasion(1);

        //TODO: make a bow draw much faster 0.5% = 0.05f
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("strength_primary");
    }
}

