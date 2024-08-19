package sypztep.penomior.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import sypztep.penomior.Penomior;
import sypztep.penomior.client.widget.*;
import sypztep.penomior.common.component.UniqueStatsComponent;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.stats.StatTypes;
import sypztep.penomior.common.util.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public final class PlayerInfoScreen extends Screen {
    // 1. Constants
    private static final float ANIMATION_DURATION = 12.0f; // Duration of the animation in seconds
    private static final float FINAL_Y_OFFSET = 50.0f; // Final vertical offset of the text

    // Animations
    private Animation verticalAnimation;
    private Animation fadeAnimation;

    // Player Stats
    private final UniqueStatsComponent playerStats;

    // UI Components
    private List<IncreasePointButton> increaseButtons;
    private List<Text> texts;
    private final CyclingTextIcon cyclingTextIcon;
    private SmoothProgressBar progessBar;
    private final ScrollableTextList playerInfo;

    // Layout and Sizing
    private final int buttonHeight = 16;
    private final int statLabelWidth = 30;
    private final int statRowHeight = 25;

    public PlayerInfoScreen(MinecraftClient client) {
        super(Text.literal("Hello"));
        assert client.player != null;
        this.playerStats = ModEntityComponents.UNIQUESTATS.get(client.player);
        Map<String, Object> infoKeys = createPlayerInfoKey(client);
        List<ListElement> listInfo = createListItems();

        this.playerInfo = new ScrollableTextList(listInfo, infoKeys);
        this.cyclingTextIcon = new CyclingTextIcon(100);
    }

    public void updateValues(MinecraftClient client) {
        Map<String, Object> values = createPlayerInfoKey(client);
        this.playerInfo.updateValues(values);
    }

    private Map<String, Object> createPlayerInfoKey(MinecraftClient client) {
        Map<String, Object> values = new HashMap<>();
        Map<String, Double> attributeAmounts = ItemStackHelper.getAttributeAmounts(client.player, playerStats.getPlayerStats().getStat(StatTypes.STRENGTH).getValue() * 0.02);

        assert client.player != null;
        double armor = client.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR);
        double armorToughness = client.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
        double attackDamage = attributeAmounts.getOrDefault("attribute.name.generic.attack_damage", client.player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)); // IDK why it not sync wtih server but who care :)

        values.put("phyd", client.player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        values.put("meeld", client.player.getAttributeValue(ModEntityAttributes.GENERIC_MELEE_ATTACK_DAMAGE));
        values.put("projd", client.player.getAttributeValue(ModEntityAttributes.GENERIC_PROJECTILE_ATTACK_DAMAGE));
        values.put("ap", attackDamage);
        values.put("asp", client.player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_SPEED));
        values.put("pvp", client.player.getAttributeValue(ModEntityAttributes.GENERIC_PLAYER_VERS_PLAYER_DAMAGE) * 100f);
        values.put("pve", client.player.getAttributeValue(ModEntityAttributes.GENERIC_PLAYER_VERS_ENTITY_DAMAGE) * 100f);
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
        values.put("mdmg", client.player.getAttributeValue(ModEntityAttributes.GENERIC_MAGIC_ATTACK_DAMAGE));
        values.put("mresis", client.player.getAttributeValue(ModEntityAttributes.GENERIC_MAGIC_RESISTANCE) * 100f);
        values.put("physis", client.player.getAttributeValue(ModEntityAttributes.GENERIC_PHYSICAL_RESISTANCE) * 100f);
        values.put("projsis", client.player.getAttributeValue(ModEntityAttributes.GENERIC_PROJECTILE_RESISTANCE) * 100f);
        return values;
    }

    private List<ListElement> createListItems() {
        List<ListElement> listElements = new ArrayList<>();
        listElements.add(new ListElement("MELEE", Penomior.id("hud/container/icon_1")));
        listElements.add(new ListElement("Physical Damage: %phyd"));
        listElements.add(new ListElement("Meele Damage: %meeld"));
        listElements.add(new ListElement("Projectile Damage: %projd"));
        listElements.add(new ListElement("Attack Power: %ap"));
        listElements.add(new ListElement("Attack Speed: %asp"));
        listElements.add(new ListElement("Accuracy: %acc"));
        listElements.add(new ListElement("Critical Damage: %cdmg %"));
        listElements.add(new ListElement("Critical Chance: %ccn %"));
        listElements.add(new ListElement("PVE Damage : %pve %"));
        listElements.add(new ListElement("PVP Damage : %pve %"));
        listElements.add(new ListElement("MAGIC", Penomior.id("hud/container/icon_0")));
        listElements.add(new ListElement("Magic Damage: %mdmg"));
        listElements.add(new ListElement("VITALITY", Identifier.ofVanilla("hud/heart/full")));
        listElements.add(new ListElement("Health: %hp"));
        listElements.add(new ListElement("Max Health: %maxhp"));
        listElements.add(new ListElement("Defense: %dp"));
        listElements.add(new ListElement("Nature Health Regen: %nhrg"));
        listElements.add(new ListElement("Evasion: %eva"));
        listElements.add(new ListElement("STATS", Identifier.ofVanilla("icon/accessibility")));
        listElements.add(new ListElement("Strength: %str"));
        listElements.add(new ListElement("Agility: %agi"));
        listElements.add(new ListElement("Vitality: %vit"));
        listElements.add(new ListElement("Intelligence: %int"));
        listElements.add(new ListElement("Dexterity: %dex"));
        listElements.add(new ListElement("Luck: %luk"));
        listElements.add(new ListElement("RESISTANCE", Penomior.id("hud/container/icon_2")));
        listElements.add(new ListElement("Magic Resistance: %mresis %"));
        listElements.add(new ListElement("Physical Resistance: %physis %"));
        listElements.add(new ListElement("Projectile Resistance: %projsis %"));
        return listElements;
    }

    @Override
    protected void init() {
        super.init();
        this.verticalAnimation = new Animation(ANIMATION_DURATION, false); // Single play for vertical animation
        this.fadeAnimation = new Animation(ANIMATION_DURATION, false); // Single play for fade animation
        this.progessBar = new SmoothProgressBar(ANIMATION_DURATION * 1.4f, false, 400, 2);
        increaseButtons = new ArrayList<>(); // Initialize the list to hold buttons

        int y = 50;
        for (StatTypes statType : StatTypes.values()) {
            int statValueWidth = 30;
            int startX = 50;
            int buttonX = startX + statLabelWidth + statValueWidth + 10; // Some spacing
            int buttonY = y;
            int buttonWidth = 16;
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

        int screenWidth = this.width;
        int screenHeight = this.height;

        float contentSectionWidthRatio = 0.25f; // 25% of screen width
        float contentSectionHeightRatio = 0.75f; // 50% of screen height

        int contentWidth = (int) (screenWidth * contentSectionWidthRatio);
        int contentHeight = (int) (screenHeight * contentSectionHeightRatio);

        int xOffset = (int) (screenWidth * 0.67f); // 2/3 of the screen width
        int yOffset = (int) AnimationUtils.getPositionOffset(verticalAnimation.getProgress(), FINAL_Y_OFFSET, screenHeight);

        // Set background color
        DrawContextUtils.fillScreen(context, 0xF0121212);

        drawStatsSection(context, xOffset, yOffset, contentWidth, contentHeight, delta);

        renderStatsAndButtons(context, screenWidth, yOffset, mouseX, mouseY, delta);

        renderBottomLeftSection(context, screenWidth, screenHeight, delta);

        renderMiddleSection(context, screenWidth, screenHeight, fadeAnimation.getProgress());

        // Draw header section
        drawHeaderSection(context, xOffset + 100, yOffset, fadeAnimation.getProgress(), "penomior.gui.player_info.header");
        drawHeaderSection(context, (int) (screenWidth * 0.025f) + 80, yOffset, fadeAnimation.getProgress(), "penomior.gui.player_info.header_level");

    }

    private void drawStatsSection(DrawContext context, int xOffset, float yOffset, int contentWidth, int contentHeight, float deltatick) {
        this.playerInfo.render(context, this.textRenderer, xOffset + 25, (int) (yOffset + 55), contentWidth, contentHeight, 0.5f, 1f, AnimationUtils.getAlpha(fadeAnimation.getProgress()), deltatick);
    }

    private void renderStyledText(DrawContext context, int x, int y, int perPoint, float scale) {
        Text perPointText = Text.of(String.valueOf(perPoint)).copy().setStyle(Style.EMPTY.withColor((0xF17633)));
        Text pointText = Text.of(" Point").copy().setStyle(Style.EMPTY.withColor(Formatting.WHITE));

        Text text = Text.empty()
                .append(perPointText)
                .append(pointText);

        // Render the styled text
        context.drawTextWithShadow(this.textRenderer, text, (int) (x / scale), (int) (y / scale), 0xFFFFFF);
    }

    private void renderStatsAndButtons(DrawContext context, int screenWidth, int yOffset, int mouseX, int mouseY, float delta) {
        int rectX = (int) (screenWidth * 0.025f); // X position of the rectangle
        int rectY = yOffset + 20;                 // Y position of the rectangle
        int y;                       // Start slightly below the top of the rectangle to add padding
        int labelX = rectX + 10;                  // Start slightly inside the left edge of the rectangle

        int buttonIndex = 0;

        // Calculate maximum width and height needed
        int maxWidth = 0;
        int totalHeight = 0;

        MatrixStack matrixStack = context.getMatrices();
        // Measure width of the widest stat label + value
        for (StatTypes statType : StatTypes.values()) {
            String label = statType.getAka() + ":";
            int labelWidth = this.textRenderer.getWidth(Text.of(label));
            int valueWidth = this.textRenderer.getWidth(Text.of(playerStats.getPlayerStats().getStat(statType).getIncreasePerPoint() + " Point"));

            // Add padding to width calculation
            maxWidth = Math.max(maxWidth, labelWidth + valueWidth + 50); // 20 is padding
            totalHeight += statRowHeight + 2; // Total height for each row
        }

        // Draw the rectangle with the calculated size
        DrawContextUtils.drawRect(context, rectX, rectY, maxWidth, totalHeight, 0xFF1E1E1E);

        // Render the content inside the rectangle
        y = rectY + 10; // Reset Y to the top of the rectangle plus padding

        for (StatTypes statType : StatTypes.values()) {
            int labelY = y + (buttonHeight - this.textRenderer.fontHeight) / 2;
            float scale = 0.9f;
            // Draw Stat Label
            matrixStack.push();
            matrixStack.scale(scale, scale, 0);
            context.drawTextWithShadow(this.textRenderer, Text.of(statType.getAka() + ":"), (int) (labelX / scale), (int) (labelY / scale), 0xFFFFFF);
            matrixStack.pop();
            DrawContextUtils.renderHorizontalLineWithCenterGradient(context, rectX, y + 20, maxWidth, 1, 1, 0xFFFFFFFF, 0x00FFFFFF);

            // Draw Stat Value
            int valueX = labelX + statLabelWidth;
            int perPoint = playerStats.getPlayerStats().getStat(statType).getIncreasePerPoint();
            matrixStack.push();
            matrixStack.scale(scale, scale, 0);
            renderStyledText(context, valueX, labelY, perPoint, scale);

            matrixStack.pop();

            // Position and render the corresponding button
            IncreasePointButton button = increaseButtons.get(buttonIndex);
            int buttonX = valueX + statLabelWidth + 12; // Position the button to the right of the stat value
            button.setX(buttonX);
            button.setY(y); // Align the button with the current row
            button.render(context, mouseX, mouseY, delta);

            // Increment Y for the next stat row
            y += statRowHeight;
            buttonIndex++;
        }
    }

    @Override
    public void tick() {
        super.tick();
        cyclingTextIcon.updateTexts(texts);
    }

    private void renderMiddleSection(DrawContext context, int screenWidth, int screenHeight, float fadeProgress) {
        int remainingPoints = playerStats.getPlayerStats().getLevelSystem().getStatPoints();
        float scaleFactor = 2.5f;
        int adjustedX = (int) (screenWidth * 0.5f / scaleFactor);
        int adjustedY = (int) (screenHeight * 0.5f / scaleFactor);

        String fuckyougramma = remainingPoints > 1 ? "Stat Points" : "Stat Point";

        context.getMatrices().push();
        context.getMatrices().scale(scaleFactor, scaleFactor, 0.0f);

        AnimationUtils.drawFadeCenteredText(context, textRenderer, Text.of("" + remainingPoints), adjustedX, adjustedY, 0xF17633, AnimationUtils.getAlpha(fadeProgress));

        context.getMatrices().pop();
        AnimationUtils.drawFadeCenteredText(context, textRenderer, Text.of(fuckyougramma),  (int) (screenWidth * 0.5f), (int) (screenHeight * 0.5f) + 25, 0xFFFFFF, AnimationUtils.getAlpha(fadeProgress));
    }


    private void renderBottomLeftSection(DrawContext context, int screenWidth, int screenHeight, float delta) {
        int labelX = (int) (screenWidth * 0.025f); // X position, a little inset from the left edge
        int labelY = screenHeight - 45; // Y position, near the bottom of the screen
        context.getMatrices().push();
        context.getMatrices().scale(0.8f, 0.8f, 1.0f);
        int scaledLabelX = (int) (labelX / 0.8f); // Adjust X position for scaling
        int scaledLabelY = (int) (labelY / 0.8f); // Adjust Y position for scaling

        texts = Arrays.asList(Text.of("Lvl Progress: " + playerStats.getXp() + "/" + playerStats.getNextXpLevel()),
                Text.of(playerStats.getNextXpLevel() - playerStats.getXp() + " XP to Level " + playerStats.getNextLevel()),
                Text.of("Level: " + playerStats.getLevel() + " " + playerStats.getXpPercentage()));

        cyclingTextIcon.render(context, textRenderer, delta, scaledLabelX, scaledLabelY + 20, 0xFFFFFF);  // Lvl Progession

        progessBar.setProgress(playerStats.getXp(), playerStats.getNextXpLevel());
        progessBar.render(context, scaledLabelX, scaledLabelY + 40);
        context.getMatrices().pop();
    }

    private void drawHeaderSection(DrawContext context, int x, float verticalOffset, float fadeProgress, String text) {
        int textWidth = this.textRenderer.getWidth(Text.translatable(text));
        int centeredX = x - (textWidth / 2);
        AnimationUtils.drawFadeText(context, this.textRenderer, Text.translatable(text), centeredX, (int) verticalOffset, AnimationUtils.getAlpha(fadeProgress));
        int lineY1 = (int) (verticalOffset - 4);
        int lineY2 = (int) (verticalOffset + 10);
        DrawContextUtils.renderHorizontalLineWithCenterGradient(context, centeredX - 16, lineY1, textWidth + 32, 1, 400, 0xFFFFFFFF, 0, fadeProgress);
        DrawContextUtils.renderHorizontalLineWithCenterGradient(context, centeredX - 16, lineY2, textWidth + 32, 1, 400, 0xFFFFFFFF, 0, fadeProgress);
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