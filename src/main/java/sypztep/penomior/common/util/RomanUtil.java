package sypztep.penomior.common.util;

import java.util.HashMap;
import java.util.Map;

public class RomanUtil {
    public static Map<Integer, String> customMap = new HashMap<>();
    static {
        customMap.put(16, "I");
        customMap.put(17, "II");
        customMap.put(18, "III");
        customMap.put(19, "IV");
        customMap.put(20, "V");
    }
}
