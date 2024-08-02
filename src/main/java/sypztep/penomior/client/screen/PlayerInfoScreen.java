package sypztep.penomior.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Text;
import sypztep.penomior.Penomior;
import sypztep.penomior.client.object.Animation;
import sypztep.penomior.client.object.ListElement;
import sypztep.penomior.client.object.ScrollableTextList;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.*;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PlayerInfoScreen extends Screen {
    private static final float ANIMATION_DURATION = 12.0f; // Duration of the animation in seconds
    private static final float FINAL_Y_OFFSET = 50.0f; // Final vertical offset of the text

    private Animation verticalAnimation;
    private Animation fadeAnimation;
//    private SmoothProgressBar progessBar;

    private final ScrollableTextList scrollableTextList;

    public PlayerInfoScreen(MinecraftClient client) {
        super(Text.literal("Hello"));
        this.client = client;
        assert client.player != null;

        Map<String, Double> attributeAmounts = ItemStackHelper.getAttributeAmounts(client.player);
        double attackDamage = attributeAmounts.getOrDefault("attribute.name.generic.attack_damage", client.player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        // Map of values for text replacement
        Map<String, Object> values = Map.of(
                "ap", attackDamage,
                "asp", client.player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED),
                "cdmg", client.player.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE) * 100f,
                "ccn", client.player.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_CHANCE) * 100f,
                "apen", 0,
                "acc", ModEntityComponents.STATS.get(client.player).getAccuracy(),
                "dp", client.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR),
                "at", client.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS),
                "dr", 0,
                "eva", ModEntityComponents.STATS.get(client.player).getEvasion()
        );

        // Create a list of ListElement objects with text and associated icons
        List<ListElement> listItems = List.of(
                new ListElement("MELEE", Penomior.id("hud/container/icon_0")),
                new ListElement("Attack Power: %ap"),
                new ListElement("Attack Speed: %asp"),
                new ListElement("Critical Damage: %cdmg %"),
                new ListElement("Critical Chance: %ccn %"),
                new ListElement("Armor Penetrate: %apen %"),
                new ListElement("Accuracy: %acc"),
                new ListElement("DEFENSE", Penomior.id("hud/container/icon_1.png")),
                new ListElement("Armor: %dp"),
                new ListElement("Armor Toughness: %at"),
                new ListElement("Damage Reduction: %dr %"),
                new ListElement("Evasion: %eva")
        );

        this.scrollableTextList = new ScrollableTextList(listItems, values); // Assuming textHeight is 25
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

        verticalAnimation.update(delta);
        fadeAnimation.update(delta);
//        progessBar.update(delta);

        // Get screen dimensions
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();

        // Calculate positions and sizes based on screen dimensions
        float contentSectionWidthRatio = 0.25f; // 25% of screen width
        float contentSectionHeightRatio = 0.5f; // 50% of screen height

        int contentWidth = (int) (screenWidth * contentSectionWidthRatio);
        int contentHeight = (int) (screenHeight * contentSectionHeightRatio);

        // Position x should be centered with some margin
        int textComponentX = (int) (screenWidth * 0.67f); // 2/3 of the screen width
        int verticalOffset = (int) AnimationUtils.getPositionOffset(verticalAnimation.getProgress(), FINAL_Y_OFFSET, screenHeight);

        // Set background color
        DrawContextUtils.fillScreen(context, 0xFF121212);

        // Draw content section
        drawContentSection(context, textComponentX, verticalOffset, contentWidth, contentHeight, screenHeight, delta);

        // Draw header section
        drawHeaderSection(context, textComponentX, verticalOffset, fadeAnimation.getProgress());

        // Define the size and position of the progress bar
//        int barWidth = 200;
//        int barHeight = 20;
//        int x = (screenWidth - barWidth) / 2;
//        int y = (screenHeight - barHeight) / 2;

//        progessBar.setProgress(0.51f);

        // Render the progress bar
//        progessBar.render(context, x, y);
    }

    private void drawContentSection(DrawContext context, int x, float verticalOffset, int contentWidth, int contentHeight, int screenHeight, float deltatick) {
        // Set the rectangle's position with margin and draw it
        int rectX = x + 10;
        int rectY = (int) verticalOffset + 14;
        DrawContextUtils.drawRect(context, rectX, rectY, contentWidth, contentHeight + 8, 0xFF1E1E1E);

        // Render scrollable text list
        scrollableTextList.render(context, this.textRenderer, x + 25, (int) (verticalOffset + 55), contentWidth, screenHeight, 0.5f, AnimationUtils.getAlpha(fadeAnimation.getProgress()), deltatick);
    }

    private void drawHeaderSection(DrawContext context, int x, float verticalOffset, float fadeProgress) {
        AnimationUtils.drawFadeText(context, this.textRenderer, Text.translatable("penomior.gui.player_info.header"), x + 60, (int) (verticalOffset), AnimationUtils.getAlpha(fadeProgress));
        DrawContextUtils.renderHorizontalLineWithCenterGradient(context, x + 48, (int) (-4 + verticalOffset), 80, 1, 400,
                ColorUtils.rgbaToHex(255, 255, 255, 255), ColorUtils.rgbaToHex(0, 0, 0, 0), fadeProgress);
        DrawContextUtils.renderHorizontalLineWithCenterGradient(context, x + 48, (int) (10 + verticalOffset), 80, 1, 400,
                ColorUtils.rgbaToHex(255, 255, 255, 255), ColorUtils.rgbaToHex(0, 0, 0, 0), fadeProgress);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollableTextList.scroll((int) verticalAmount * 25);

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}