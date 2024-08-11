package sypztep.penomior.common.stats;

public class VitalityStat extends Stat {
    public VitalityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }
}
