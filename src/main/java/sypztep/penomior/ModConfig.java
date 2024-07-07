package sypztep.penomior;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;

public class ModConfig extends MidnightConfig {
    @Comment(centered = true)
    public static Comment iframeConfig;
    public static List<String> attackExcludedEntities = List.of("minecraft:slime", "minecraft:magma_cube");
    @Entry
    public static float attackCancelThreshold = 0.15f;
    @Entry
    public static float knockbackCancelThreshold = 0.75f;
    @Entry
    public static int iFrameDuration = 10;
    static {
        MidnightConfig.init(Penomior.MODID, ModConfig.class);
    }
}
