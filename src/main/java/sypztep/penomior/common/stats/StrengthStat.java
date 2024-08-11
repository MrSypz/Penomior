package sypztep.penomior.common.stats;

public class StrengthStat extends Stat {
    public StrengthStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }
}

