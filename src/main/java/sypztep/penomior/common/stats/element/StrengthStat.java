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

public class StrengthStat extends Stat {
    public StrengthStat(int baseValue) {
        super(baseValue);
    }


    @Override
    public void applyPrimaryEffect(ServerPlayerEntity player) {
        applyEffect(player,
                ModEntityAttributes.GENERIC_MELEE_ATTACK_DAMAGE,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> (0.02 * this.currentValue)
        );
    }

    @Override
    public void applySecondaryEffect(ServerPlayerEntity player) {
        List<AttributeModification> modifications = List.of(
                new AttributeModification(
                        ModEntityAttributes.GENERIC_CRIT_CHANCE,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.005 * this.currentValue)
                ),
                new AttributeModification(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.002 * this.currentValue)
                )
        );

        applyEffects(player, modifications);
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("strength_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("strength_secondary");
    }
}

