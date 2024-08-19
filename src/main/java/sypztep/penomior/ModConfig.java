package sypztep.penomior;


import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.Arrays;
import java.util.List;
@Config(name = Penomior.MODID)
public class ModConfig implements ConfigData {
    @Comment("Excluded the damage intake that by-pass Iframe")
    @ConfigEntry.Category("gameplay")
    public static List<String> dmgReceiveExcludedEntities = Arrays.asList(
            "minecraft:slime", "minecraft:magma_cube"
    );
    @Comment("Excluded the Entities that by-pass Iframe")
    @ConfigEntry.Category("gameplay")
    public static List<String> attackExcludedEntities = List.of(
            "minecraft:warden"
    );
    @ConfigEntry.Category("gameplay")
    @Comment("How weak a player's attack can be before it gets nullified, from 0 (0%, cancels multiple attacks on the same tick) to 1 (100%, players cannot attack), or -0.1 (disables this feature)")
    public static float attackCancelThreshold = 0.15f;
    @ConfigEntry.Category("gameplay")
    @Comment("How weak a player's attack can be before the knockback gets nullified, from 0 (0%, cancels multiple attacks on the same tick) to 1 (100%, no knockback), or -0.1 (disables this feature)")
    public static float knockbackCancelThreshold = 0.75f;
    @Comment("How many ticks of i-frames does an entity get when damaged, from 10 (default) 0.5 sec, to 2^31-1 (nothing can take damage)")
    @ConfigEntry.Category("gameplay")
    public static int iFrameDuration = 10;
    @ConfigEntry.Category("refinement")
    public static boolean refineItemUnbreak = true;

    @ConfigEntry.Category("feature-client")
    public static boolean missingIndicator = true;
    @ConfigEntry.Category("feature-client")
    public static boolean damageIndicator = true;
    @ConfigEntry.Category("feature-client")
    public static boolean xpnotify = false;
    @ConfigEntry.Category("feature-client")
    public static boolean lossxpnotify = true;
    @ConfigEntry.Category("feature")
    public static boolean missingArrowPassthough = true;

    @ConfigEntry.Category("statconfig")
    public static RenderStyle renderStyle = RenderStyle.SLATE;
    @ConfigEntry.Category("statconfig")
    public static boolean tooltipinfo = true;
    @ConfigEntry.Category("statconfig")
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public static int barColor = 0xFFFFFFFF;
    @ConfigEntry.Category("statconfig")
    @ConfigEntry.ColorPicker(allowAlpha = true)
    public static int barBGColor = 0xFFAB5C00;

    public enum RenderStyle {
        BAR,
        SLATE
    }
}
