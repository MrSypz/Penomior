package sypztep.penomior.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import sypztep.penomior.client.object.Animation;
import sypztep.penomior.client.object.ScrollableTextList;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerInfoScreen extends Screen {
    private static final float ANIMATION_DURATION = 12.0f; // Duration of the animation in seconds
    private static final float FINAL_Y_OFFSET = 50.0f; // Final vertical offset of the text

    private Animation verticalAnimation;
    private Animation fadeAnimation;
    private final ScrollableTextList scrollableTextList;

    public PlayerInfoScreen(MinecraftClient client) {
        super(Text.literal("Hello"));
        this.client = client;
        assert client.player != null;

        Map<String, Double> attributeAmounts = ItemStackHelper.getAttributeAmounts(client.player);
        double attackDamage = attributeAmounts.getOrDefault("attribute.name.generic.attack_damage", client.player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));

        Map<String, Object> values = Map.of(
                "ap", attackDamage,
                "asp", client.player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED),
                "cdmg", client.player.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE),
                "ccn", client.player.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_CHANCE),
                "dp", client.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR)
        );

        List<String> element = new ArrayList<>(List.of(
                "MELEE",
                "Attack Power: %ap",
                "Attack Speed: %asp",
                "Critical Damage: %cdmg",
                "Critical Chance: %ccn",
                "DEFENSE",
                "Armor: %dp"
        ));
        this.scrollableTextList = new ScrollableTextList(element, values);
    }

    @Override
    protected void init() {
        super.init();
        this.verticalAnimation = new Animation(ANIMATION_DURATION, false); // Single play for vertical animation
        this.fadeAnimation = new Animation(ANIMATION_DURATION, false); // Single play for fade animation
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        if (!verticalAnimation.isCompleted()) verticalAnimation.update(delta);
        if (!fadeAnimation.isCompleted()) fadeAnimation.update(delta);

        DrawContextUtils.fillScreen(context, 0xFF121212);

        float verticalOffset = AnimationUtils.getPositionOffset(verticalAnimation.getProgress(), FINAL_Y_OFFSET);
        int alpha = AnimationUtils.getAlpha(fadeAnimation.getProgress());
        float fadeProgress = fadeAnimation.getProgress();

        Vec2f scale = new Vec2f(1.0f, 1.0f);

        drawContentSection(context, 1280, (int) verticalOffset, alpha, scale);
        drawHeaderSection(context, 1280, (int) verticalOffset, alpha, fadeProgress, scale);
    }

    private void drawContentSection(DrawContext context, int x, float verticalOffset, int alpha, Vec2f scale) {
        int contentHeight = 810; // Equivalent to 1.5 times the base height
        int contentWidth = 480; // Equivalent to one-fourth the base width

        PositionElementUtil.drawScaledRect(context, x + 20, (int) verticalOffset - 10, contentWidth, contentHeight + 6, 0xFF1E1E1E, scale);

        scrollableTextList.render(context, this.textRenderer,
                PositionElementUtil.getScaledX(context.getScaledWindowWidth(), x + 25, scale.x),
                PositionElementUtil.getScaledY(context.getScaledWindowHeight(), (int) verticalOffset + 45, scale.y),
                PositionElementUtil.getScaledWidth(context.getScaledWindowWidth(), contentWidth, scale.x),
                PositionElementUtil.getScaledHeight(context.getScaledWindowHeight(), contentHeight * 2, scale.y),
                0.5f, alpha);
    }

    private void drawHeaderSection(DrawContext context, int x, float verticalOffset, int alpha, float fadeProgress, Vec2f scale) {
        AnimationUtils.drawFadeText(context, this.textRenderer, "Information",
                PositionElementUtil.getScaledX(context.getScaledWindowWidth(), x + 60, scale.x),
                PositionElementUtil.getScaledY(context.getScaledWindowHeight(), (int) (-25 + verticalOffset), scale.y), alpha);

        DrawContextUtils.renderHorizontalLineWithCenterGradient(context,
                PositionElementUtil.getScaledX(context.getScaledWindowWidth(), x + 48, scale.x),
                PositionElementUtil.getScaledY(context.getScaledWindowHeight(), (int) (-15 + verticalOffset), scale.y),
                PositionElementUtil.getScaledWidth(context.getScaledWindowWidth(), 80, scale.x), 1, 400,
                ColorUtils.rgbaToHex(255, 255, 255, 255), ColorUtils.rgbaToHex(0, 0, 0, 0), fadeProgress);

        DrawContextUtils.renderVerticalLine(context,
                PositionElementUtil.getScaledX(context.getScaledWindowWidth(), x, scale.x),
                PositionElementUtil.getScaledY(context.getScaledWindowHeight(), (int) (-25 + verticalOffset), scale.y),
                PositionElementUtil.getScaledHeight(context.getScaledWindowHeight(), 226, scale.y), 1, 400, ColorUtils.fromRgb(255, 255, 255));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollableTextList.scroll((int) verticalAmount * 20);
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}







