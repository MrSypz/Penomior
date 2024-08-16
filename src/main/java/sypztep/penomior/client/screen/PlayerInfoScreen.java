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
import sypztep.penomior.client.object.*;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.Stat;
import sypztep.penomior.common.stats.StatTypes;
import sypztep.penomior.common.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PlayerInfoScreen extends Screen {
    private static final float ANIMATION_DURATION = 12.0f; // Duration of the animation in seconds
    private static final float FINAL_Y_OFFSET = 50.0f; // Final vertical offset of the text

    private Animation verticalAnimation;
    private Animation fadeAnimation;
    private final UniqueStatsComponent playerStats;
    private List<IncreasePointButton> increaseButtons;

    private SmoothProgressBar progessBar;

    private final int buttonWidth = 16;
    private final int buttonHeight = 16;
    private final int statLabelWidth = 30;
    private final int statValueWidth = 30;
    private final int statRowHeight = 25;
    private final int startX = 50;
    private final int startY = 50;

    private final ScrollableTextList playerInfo;

    public PlayerInfoScreen(MinecraftClient client) {
        super(Text.literal("Hello"));
        assert client.player != null;
        this.playerStats = ModEntityComponents.UNIQUESTATS.get(client.player);
        Map<String, Object> infoKeys = createPlayerInfoKey(client);
        List<ListElement> listInfo = createListItems();

        this.playerInfo = new ScrollableTextList(listInfo, infoKeys);
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
        values.put("nhrg", client.player.getAttributeValue(ModEntityAttributes.GENERIC_HEALTH_REGEN));
        values.put("eva", ModEntityComponents.STATS.get(client.player).getEvasion());
        values.put("str", playerStats.getPlayerStats().getStat(StatTypes.STRENGTH).getValue());
        values.put("agi", playerStats.getPlayerStats().getStat(StatTypes.AGILITY).getValue());
        values.put("vit", playerStats.getPlayerStats().getStat(StatTypes.VITALITY).getValue());
        values.put("int", playerStats.getPlayerStats().getStat(StatTypes.INTELLIGENCE).getValue());
        values.put("dex", playerStats.getPlayerStats().getStat(StatTypes.DEXTERITY).getValue());
        values.put("luk", playerStats.getPlayerStats().getStat(StatTypes.LUCK).getValue());
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
                new ListElement("Evasion: %eva"),
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
        this.progessBar = new SmoothProgressBar(ANIMATION_DURATION * 1.4f, false, 200, 20, 0xFF00FF00, 0xFF1E1E1E);
        increaseButtons = new ArrayList<>(); // Initialize the list to hold buttons

        int y = startY;
        for (StatTypes statType : StatTypes.values()) {
            int buttonX = startX + statLabelWidth + statValueWidth + 10; // Some spacing
            int buttonY = y;
            IncreasePointButton increaseButton = new IncreasePointButton(buttonX, buttonY, buttonWidth, buttonHeight, Text.of("+"), playerStats, statType);
            this.addDrawableChild(increaseButton);
            increaseButtons.add(increaseButton);

            y += statRowHeight; // Move to the next row
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        assert client != null;
        updateValues(client);

        verticalAnimation.update(delta);
        fadeAnimation.update(delta);
        progessBar.update(delta);

        // Get screen dimensions from the Screen class
        int screenWidth = this.width;
        int screenHeight = this.height;

        // Calculate positions and sizes based on screen dimensions
        float contentSectionWidthRatio = 0.25f; // 25% of screen width
        float contentSectionHeightRatio = 0.75f; // 50% of screen height

        int contentWidth = (int) (screenWidth * contentSectionWidthRatio);
        int contentHeight = (int) (screenHeight * contentSectionHeightRatio);

        // Position x should be centered with some margin
        int xOffset = (int) (screenWidth * 0.67f); // 2/3 of the screen width
        int yOffset = (int) AnimationUtils.getPositionOffset(verticalAnimation.getProgress(), FINAL_Y_OFFSET, screenHeight);

        // Set background color
        DrawContextUtils.fillScreen(context, 0xFF121212);

        // Draw content section
        drawStatsSection(context, xOffset, yOffset, contentWidth, contentHeight, delta);

        renderStatsAndButtons(context, screenWidth, yOffset, mouseX, mouseY, delta);

        // Draw header section
        drawHeaderSection(context, xOffset + 100, yOffset, fadeAnimation.getProgress(), "penomior.gui.player_info.header");
        drawHeaderSection(context, (int) (screenWidth * 0.025f) + 80, yOffset, fadeAnimation.getProgress(), "penomior.gui.player_info.header_level");

        // Define the size and position of the progress bar
//        int barWidth = 200;
//        int barHeight = 20;
//        int x = (screenWidth - barWidth) / 2;
//        int y = (screenHeight - barHeight) / 2;
//
//        progessBar.setProgress(0.51f);
//
////         Render the progress bar
//        progessBar.render(context, x, y);
    }

    private void drawStatsSection(DrawContext context, int xOffset, float yOffset, int contentWidth, int contentHeight, float deltatick) {
        this.playerInfo.render(context, this.textRenderer, xOffset + 25, (int) (yOffset + 55), contentWidth, contentHeight, 0.5f, 1f, AnimationUtils.getAlpha(fadeAnimation.getProgress()), deltatick);
    }
    private void renderStatsAndButtons(DrawContext context, int screenWidth, int yOffset, int mouseX, int mouseY, float delta) {
        int rectX = (int) (screenWidth * 0.025f); // X position of the rectangle
        int rectY = yOffset + 25;                 // Y position of the rectangle
        int y = rectY + 10;                       // Start slightly below the top of the rectangle to add padding
        int labelX = rectX + 10;                  // Start slightly inside the left edge of the rectangle

        int buttonIndex = 0;

        for (StatTypes statType : StatTypes.values()) {
            int labelY = y + (buttonHeight - this.textRenderer.fontHeight) / 2;

            // Draw Stat Label
            context.drawTextWithShadow(this.textRenderer, Text.of(statType.getAka() + ":"), labelX, labelY, 0xFFFFFF);

            // Draw Stat Value
            int valueX = labelX + statLabelWidth;
            int perPoint = playerStats.getPlayerStats().getStat(statType).getIncreasePerPoint();
            context.drawTextWithShadow(this.textRenderer, Text.of(perPoint + " SP"), valueX, labelY, 0xFFFFFF);

            // Position and render the corresponding button
            IncreasePointButton button = increaseButtons.get(buttonIndex);
            int buttonX = valueX + statLabelWidth; // Position the button to the right of the stat value
            button.setX(buttonX);
            button.setY(y); // Align the button with the current row
            button.render(context, mouseX, mouseY, delta);

            // Increment Y for the next stat row
            y += statRowHeight;
            buttonIndex++;
        }

        // Draw Remaining Stat Points
        int remainingPoints = playerStats.getPlayerStats().getLevelSystem().getStatPoints();
        String remainingText = "Remaining Stat Points: " + remainingPoints;
        int remainingTextY = y + 20;
        context.drawTextWithShadow(this.textRenderer, remainingText, labelX, remainingTextY, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Level Progess: " + playerStats.getXp(), labelX, remainingTextY + 20, 0xFFFFFF);
//        Penomior.LOGGER.info(String.valueOf(playerStats.getXp()));
    }


    private void drawHeaderSection(DrawContext context, int x, float verticalOffset, float fadeProgress, String text) {
        int textWidth = this.textRenderer.getWidth(Text.translatable(text));
        int centeredX = x - (textWidth / 2);
        AnimationUtils.drawFadeText(context, this.textRenderer, Text.translatable(text), centeredX, (int) verticalOffset, AnimationUtils.getAlpha(fadeProgress));
        int lineY1 = (int) (verticalOffset - 4);
        int lineY2 = (int) (verticalOffset + 10);
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
        if (playerInfo.isMouseOver(mouseX, mouseY, playerInfo.getX(), playerInfo.getY() - 30, playerInfo.getWidth(), playerInfo.getHeight())) {
            playerInfo.scroll(scrollAmount, mouseX, mouseY + 30);
            isAnyScrolled = true;
        }
        return isAnyScrolled || super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }


    @Override
    public boolean shouldPause() {
        return false;
    }
}