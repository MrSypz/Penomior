package sypztep.penomior.common.stats;

public class DexterityStat extends Stat {

    public DexterityStat(int baseValue) {
        super(baseValue);
    }

    @Override
    public void increase(int points) {
        this.currentValue += points * increasePerPoint;
    }
}
