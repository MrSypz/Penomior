package sypztep.penomior.common.stats;

public class AgilityStat extends Stat {
    public AgilityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }
}

