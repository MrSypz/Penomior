package sypztep.penomior.common.stats.element;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.Stat;
import sypztep.penomior.common.util.AttributeModification;

import java.util.List;

public class LuckStat extends Stat {
    public LuckStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(ServerPlayerEntity player) {
        applyEffect(player,
                ModEntityAttributes.GENERIC_CRIT_CHANCE,
                getPrimaryId(),
                EntityAttributeModifier.Operation.ADD_VALUE,
                baseValue -> (0.01 * this.currentValue)
        );
    }

    @Override
    public void applySecondaryEffect(ServerPlayerEntity  player) {
        List<AttributeModification> modifications = List.of(
                new AttributeModification(
                        ModEntityAttributes.GENERIC_MAGIC_ATTACK_DAMAGE,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.002 * this.currentValue)
                ),
                new AttributeModification(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.002 * this.currentValue)
                )
        );

        applyEffects(player, modifications);
        var statsComponent = ModEntityComponents.STATS.get(player);
        if (this.currentValue != 0 && this.currentValue % 5 == 0) {
            statsComponent.addExtraAccuracy(1);
            statsComponent.addExtraEvasion(1);
        }
    }

    @Override
    protected Identifier getPrimaryId() {
        return Penomior.id("luck_primary");
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("luck_secondary");
    }

}
