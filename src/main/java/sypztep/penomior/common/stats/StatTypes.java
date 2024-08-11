package sypztep.penomior.common.stats;

public enum StatTypes {
    STRENGTH("strength", StrengthStat::new),
    AGILITY("agility", AgilityStat::new),
    VITALITY("vitality", VitalityStat::new),
    INTELLIGENCE("intelligence", IntelligenceStat::new),
    DEXTERITY("dexterity", DexterityStat::new),
    LUCK("luck", LuckStat::new);

    private final String name;
    private final StatSupplier supplier;

    StatTypes(String name, StatSupplier supplier) {
        this.name = name;
        this.supplier = supplier;
    }

    public String getName() {
        return name;
    }

    public Stat createStat(int initialValue) {
        return supplier.create(initialValue);
    }

    @FunctionalInterface
    private interface StatSupplier {
        Stat create(int initialValue);
    }
}
