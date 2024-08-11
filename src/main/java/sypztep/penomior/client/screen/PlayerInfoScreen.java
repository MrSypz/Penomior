package sypztep.penomior.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.client.object.Animation;
import sypztep.penomior.client.object.IncreasePointButton;
import sypztep.penomior.client.object.ListElement;
import sypztep.penomior.client.object.ScrollableTextList;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.StatTypes;
import sypztep.penomior.common.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PlayerInfoScreen extends Screen {
    private static final float ANIMATION_DURATION = 12.0f; // Duration of the animation in seconds
    private static final float FINAL_Y_OFFSET = 50.0f; // Final vertical offset of the text

    private Animation verticalAnimation;
    private Animation fadeAnimation;
    private IncreasePointButton increaseStrengthButton;
    private final UniqueStatsComponent playerStats;
//    private SmoothProgressBar progessBar;

    private final ScrollableTextList playerInfo;
    private final ScrollableTextList playerStatsInfo;

    public PlayerInfoScreen(MinecraftClient client) {
        super(Text.literal("Hello"));
        assert client.player != null;
        this.playerStats = ModEntityComponents.UNIQUESTATS.get(client.player);
        Map<String, Object> infoKeys = createPlayerInfoKey(client);
        Map<String, Object> playerStatsKey = createPlayerStatsKey(client);
        List<ListElement> listInfo = createListItems();
        List<ListElement> listStats = createPlayerStatsList();
        this.playerInfo = new ScrollableTextList(listInfo, infoKeys);
        this.playerStatsInfo = new ScrollableTextList(listStats, playerStatsKey);
    }

    public void updateValues(MinecraftClient client) {
        Map<String, Object> values = createPlayerInfoKey(client);
        this.playerInfo.updateValues(values);
    }

    private Map<String, Object> createPlayerInfoKey(MinecraftClient client) {
        Map<String, Object> values = new HashMap<>();
        Map<String, Double> attributeAmounts = ItemStackHelper.getAttributeAmounts(client.player);

        assert client.player != null;
        double attackDamage = attributeAmounts.getOrDefault("attribute.name.generic.attack_damage",
                client.player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        double armor = client.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR);
        double armorToughness = client.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);

        values.put("ap", attackDamage);
        values.put("asp", client.player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED));
        values.put("cdmg", client.player.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE) * 100f);
        values.put("ccn", client.player.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_CHANCE) * 100f);
        values.put("acc", ModEntityComponents.STATS.get(client.player).getAccuracy());
        values.put("hp", client.player.getHealth());
        values.put("maxhp", client.player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH));
        values.put("dp", armor + (2.0f + armorToughness / 4.0f));
        values.put("nhrg", client.player.getAttributeValue(ModEntityAttributes.GENERIC_HEALTH_REGEN) * 100f);
        values.put("eva", ModEntityComponents.STATS.get(client.player).getEvasion());
        return values;
    }
    private Map<String, Object> createPlayerStatsKey(MinecraftClient client) {
        Map<String, Object> values = new HashMap<>();
        assert client.player != null;

        values.put("str", playerStats.getPlayerStats().getStat(StatTypes.STRENGTH).getCurrentValue());
        values.put("agi", playerStats.getPlayerStats().getStat(StatTypes.AGILITY).getCurrentValue());
        values.put("vit", playerStats.getPlayerStats().getStat(StatTypes.VITALITY).getCurrentValue());
        values.put("int", playerStats.getPlayerStats().getStat(StatTypes.INTELLIGENCE).getCurrentValue());
        values.put("dex", playerStats.getPlayerStats().getStat(StatTypes.DEXTERITY).getCurrentValue());
        values.put("luk", playerStats.getPlayerStats().getStat(StatTypes.LUCK).getCurrentValue());

        return values;
    }

    private List<ListElement> createListItems() {
        return List.of(
                new ListElement("MELEE", Penomior.id("hud/container/icon_0")),
                new ListElement("Attack Power: %ap"),
                new ListElement("Attack Speed: %asp"),
                new ListElement("Critical Damage: %cdmg %"),
                new ListElement("Critical Chance: %ccn %"),
                new ListElement("Accuracy: %acc"),
                new ListElement("VITALITY", Identifier.ofVanilla("hud/heart/full")),
                new ListElement("Health: %hp"),
                new ListElement("Max Health: %maxhp"),
                new ListElement("Defense: %dp"),
                new ListElement("Nature Health Regen: %nhrg"),
                new ListElement("Evasion: %eva")
        );
    }
    private List<ListElement> createPlayerStatsList() {
        return List.of(
                new ListElement("STATS", Penomior.id("hud/container/icon_10")),
                new ListElement("Strength: %str"),
                new ListElement("Agility: %agi"),
                new ListElement("Vitality: %vit"),
                new ListElement("Intelligence: %int"),
                new ListElement("Dexterity: %dex"),
                new ListElement("Luck: %luk")
        );
    }
    @Override
    protected void init() {
        super.init();
        this.verticalAnimation = new Animation(ANIMATION_DURATION, false); // Single play for vertical animation
        this.fadeAnimation = new Animation(ANIMATION_DURATION, false); // Single play for fade animation
//        this.progessBar = new SmoothProgressBar(ANIMATION_DURATION * 1.4f, false, 200, 20, 0xFF00FF00, 0xFF1E1E1E);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        assert client != null;
        updateValues(client);

        verticalAnimation.update(delta);
        fadeAnimation.update(delta);
//        progessBar.update(delta);

        // Get screen dimensions from the Screen class
        int screenWidth = this.width;
        int screenHeight = this.height;

        // Calculate positions and sizes based on screen dimensions
        float contentSectionWidthRatio = 0.25f; // 25% of screen width
        float contentSectionHeightRatio = 0.5f; // 50% of screen height

        int contentWidth = (int) (screenWidth * contentSectionWidthRatio);
        int contentHeight = (int) (screenHeight * contentSectionHeightRatio);

        // Position x should be centered with some margin
        int xOffset = (int) (screenWidth * 0.67f); // 2/3 of the screen width
        int yOffset = (int) AnimationUtils.getPositionOffset(verticalAnimation.getProgress(), FINAL_Y_OFFSET, screenHeight);

        // Set background color
        DrawContextUtils.fillScreen(context, 0xFF121212);

        // Draw content section
        drawContentSection(context, xOffset, yOffset, contentWidth, contentHeight, delta,mouseX,mouseY);
        drawContentStats(context, xOffset, yOffset + screenHeight, contentWidth, contentHeight / 2, delta,mouseX,mouseY);
        // Draw header section
        drawHeaderSection(context, xOffset + 100, yOffset, fadeAnimation.getProgress(),"penomior.gui.player_info.header");
        drawHeaderSection(context, xOffset + 100, yOffset + contentHeight + 30, fadeAnimation.getProgress(),"penomior.gui.player_info.header_stats");
        // TODO:use player stat point to render!
        int[] xPoints = {100, 150, 200};
        int[] yPoints = {100, 50, 100};

        // Example usage of drawFilledPolygon
        DrawContextUtils.drawFilledPolygon(context, xOffset, yOffset, xPoints, yPoints, ColorUtils.rgbaToHex(31,37,66,255)); // Green fill with 4-pixel border
//        increaseStrengthButton.render(context,mouseX,mouseY,delta);

        // Define the size and position of the progress bar
//        int barWidth = 200;
//        int barHeight = 20;
//        int x = (screenWidth - barWidth) / 2;
//        int y = (screenHeight - barHeight) / 2;

//        progessBar.setProgress(0.51f);

        // Render the progress bar
//        progessBar.render(context, x, y);
    }
    private void drawContentStats(DrawContext context, int xOffset, float yOffset, int contentWidth, int contentHeight, float deltatick,int mouseX,int mouseY) {
        this.playerStatsInfo.render(context, this.textRenderer, xOffset + 25, (int) yOffset + 75, contentWidth, contentHeight, 0.5f, 1f, AnimationUtils.getAlpha(fadeAnimation.getProgress()), deltatick,mouseX,mouseY);
    }

    private void drawContentSection(DrawContext context, int xOffset, float yOffset, int contentWidth, int contentHeight, float deltatick,int mouseX,int mouseY) {
        this.playerInfo.render(context, this.textRenderer, xOffset + 25, (int) (yOffset + 55), contentWidth, contentHeight, 0.5f,1f, AnimationUtils.getAlpha(fadeAnimation.getProgress()), deltatick,mouseX,mouseY);
    }

    private void drawHeaderSection(DrawContext context, int x, float verticalOffset, float fadeProgress, String text) {
        int textWidth = this.textRenderer.getWidth(Text.translatable(text));

        int centeredX = x - (textWidth / 2);

        AnimationUtils.drawFadeText(context, this.textRenderer, Text.translatable(text), centeredX, (int) verticalOffset, AnimationUtils.getAlpha(fadeProgress));

        int lineY1 = (int) (verticalOffset - 4);  // Adjust as needed
        int lineY2 = (int) (verticalOffset + 10); // Adjust as needed

        DrawContextUtils.renderHorizontalLineWithCenterGradient(
                context, centeredX - 16, lineY1, textWidth + 32, 1, 400,
                ColorUtils.rgbaToHex(255, 255, 255, 255), ColorUtils.rgbaToHex(0, 0, 0, 0), fadeProgress
        );
        DrawContextUtils.renderHorizontalLineWithCenterGradient(
                context, centeredX - 16, lineY2, textWidth + 32, 1, 400,
                ColorUtils.rgbaToHex(255, 255, 255, 255), ColorUtils.rgbaToHex(0, 0, 0, 0), fadeProgress
        );
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        boolean isAnyScrolled = false;
        int scrollAmount = (int) (verticalAmount * 25);
        // Check if the mouse is over the playerInfo list and scroll it
        if (playerInfo.isMouseOver(mouseX, mouseY, playerInfo.getX(), playerInfo.getY() - 30, playerInfo.getWidth(), playerInfo.getHeight())) {
            playerInfo.scroll(scrollAmount, mouseX, mouseY + 30);
            isAnyScrolled = true;
        }

        // Check if the mouse is over the playerStatsInfo list and scroll it
        if (playerStatsInfo.isMouseOver(mouseX, mouseY, playerStatsInfo.getX(), playerStatsInfo.getY() - 160, playerStatsInfo.getWidth(), playerStatsInfo.getHeight())) {
            playerStatsInfo.scroll(scrollAmount, mouseX, mouseY + 160);
            isAnyScrolled = true;
        }

        // Return true if any of the scrollable areas were scrolled, or pass to superclass
        return isAnyScrolled || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }



    @Override
    public boolean shouldPause() {
        return false;
    }
}