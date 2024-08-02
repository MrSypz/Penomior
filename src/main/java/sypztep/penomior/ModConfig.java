package sypztep.penomior;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.Arrays;
import java.util.List;

public class ModConfig extends MidnightConfig {
    @Comment(centered = true)
    public static Comment iframeConfig;
    public static List<String> dmgReceiveExcludedEntities = Arrays.asList(
            "minecraft:slime", "minecraft:magma_cube"
    );
    public static List<String> attackExcludedEntities = List.of(
            "minecraft:warden"
    );
    @Entry
    public static float attackCancelThreshold = 0.15f;
    @Entry
    public static float knockbackCancelThreshold = 0.75f;
    @Entry
    public static boolean refineItemUnbreak = false;
    @Entry
    public static int iFrameDuration = 10;
    @Comment(centered = true)
    public static Comment combatConfig;
    @Entry
    public static boolean backattack = true;
    @Comment(centered = true)
    public static Comment featureConfig;
    @Entry
    public static boolean inDev = false;
    @Entry
    public static boolean missingArrowPassthough = true;
    static {
        MidnightConfig.init(Penomior.MODID, ModConfig.class);
    }
}
