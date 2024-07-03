package sypztep.penomior.common.util;

import java.util.HashMap;
import java.util.Map;

public class RefineUtil {
    public static Map<Integer, String> refineMap = new HashMap<>();
    static {
        refineMap.put(16, "I");
        refineMap.put(17, "II");
        refineMap.put(18, "III");
        refineMap.put(19, "IV");
        refineMap.put(20, "V");
    }
}
