package sypztep.penomior.common.stats;

public class IntelligenceStat extends Stat {
    public IntelligenceStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }
}
