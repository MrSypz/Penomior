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

public class DexterityStat extends Stat {

    public DexterityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void applyPrimaryEffect(ServerPlayerEntity player) {
        var accuracy = ModEntityComponents.STATS.get(player);
        accuracy.addExtraAccuracy(1);
    }

    @Override
    public void applySecondaryEffect(ServerPlayerEntity  player) {
        List<AttributeModification> modifications = List.of(
                new AttributeModification(
                        ModEntityAttributes.GENERIC_PROJECTILE_ATTACK_DAMAGE,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.02 * this.currentValue)
                ),
                new AttributeModification(
                        EntityAttributes.GENERIC_ATTACK_SPEED,
                        getSecondaryId(),
                        EntityAttributeModifier.Operation.ADD_VALUE,
                        baseValue -> (0.005 * this.currentValue)
                )
        );

        applyEffects(player, modifications);
    }

    @Override
    protected Identifier getSecondaryId() {
        return Penomior.id("dexterity_secondary");
    }
}
