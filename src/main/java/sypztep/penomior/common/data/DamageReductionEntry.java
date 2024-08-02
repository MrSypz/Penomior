package sypztep.penomior.common.data;


import java.util.Map;
import java.util.TreeMap;

public record DamageReductionEntry(int point, float percentage) {
    public static final Map<Integer, Float> DAMAGEREDUCTION_ENTRY_MAP= new TreeMap<>();
}
