package sypztep.penomior.common.stats.element;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.stats.Stat;
import sypztep.penomior.common.util.AttributeModification;

import java.util.List;


public class VitalityStat extends Stat {
    public VitalityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(ServerPlayerEntity player) {
        applyEffect(
                player,
                EntityAttributes.GENERIC_MAX_HEALTH,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> baseValue * (0.05 * this.currentValue)
        );
    }

    @Override
    public void applySecondaryEffect(ServerPlayerEntity player) {
        List<AttributeModification> modifications = List.of(
                new AttributeModification(
                        ModEntityAttributes.GENERIC_HEALTH_REGEN,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.02 * this.currentValue)
                ),
                new AttributeModification(
                        ModEntityAttributes.GENERIC_PHYSICAL_RESISTANCE,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.005 * this.currentValue)
                )
        );
        applyEffects(player, modifications);
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("vitality_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("vitality_secondary");
    }
}
