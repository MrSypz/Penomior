package sypztep.penomior.common.stats.element;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.stats.Stat;

public class IntelligenceStat extends Stat {
    public IntelligenceStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(ServerPlayerEntity player) {
        applyEffect(
                player,
                ModEntityAttributes.GENERIC_MAGIC_ATTACK_DAMAGE,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> 0.02 * this.currentValue
        );
    }

    @Override
    public void applySecondaryEffect(ServerPlayerEntity  player) {
        applyEffect(
                player,
                ModEntityAttributes.GENERIC_MAGIC_RESISTANCE,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> 0.005 * this.currentValue
        );
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("intelligence_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("intelligence_secondary");
    }
}
