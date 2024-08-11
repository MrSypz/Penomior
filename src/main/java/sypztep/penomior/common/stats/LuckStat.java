package sypztep.penomior.common.stats;

public class LuckStat extends Stat {
    public LuckStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }
}
