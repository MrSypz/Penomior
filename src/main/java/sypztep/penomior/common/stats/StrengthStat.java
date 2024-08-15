package sypztep.penomior.common.stats;

import net.minecraft.entity.player.PlayerEntity;

public class StrengthStat extends Stat {
    public StrengthStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }

    @Override
    public void applyPrimaryEffect(PlayerEntity player) {

    }

    @Override
    public void applySecondaryEffect(PlayerEntity player) {

    }
}

