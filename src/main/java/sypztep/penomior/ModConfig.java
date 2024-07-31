package sypztep.penomior;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.Arrays;
import java.util.List;

public class ModConfig extends MidnightConfig {
    @Comment(centered = true)
    public static Comment iframeConfig;
    @Entry
    public static List<String> dmgReceiveExcludedEntities = Arrays.asList(
            "minecraft:slime", "minecraft:magma_cube"
    );
    @Entry
    public static List<String> attackExcludedEntities = List.of(
            "minecraft:warden"
    );
    @Entry(min = 0.0f, max = 1.0f)
    public static float attackCancelThreshold = 0.15f;

    @Entry(min = 0.0f, max = 1.0f)
    public static float knockbackCancelThreshold = 0.75f;
    @Entry
    public static boolean refineItemUnbreak = false;
    @Entry(min = -1)
    public static int iFrameDuration = 10;
    @Comment(centered = true)
    public static Comment combatConfig;
    @Entry
    public static boolean backattack = true;
    @Comment(centered = true)
    public static Comment featureConfig;
    @Entry
    public static boolean missingArrowPassthough = true;
    static {
        MidnightConfig.init(Penomior.MODID, ModConfig.class);
    }
}
