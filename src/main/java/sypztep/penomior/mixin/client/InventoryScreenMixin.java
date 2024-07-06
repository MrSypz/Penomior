package sypztep.penomior.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModEntityComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    @Unique
    private static final String PLAYER_INFO_KEY = Penomior.MODID + ".gui.player_info.";
    @Unique
    private static final int TEXTURE_SIZE = 128;
    @Unique
    private static final Identifier PLAYERINFO_TEXTURE = Penomior.id("gui/container/player_info.png");

    @Shadow @Final private RecipeBookWidget recipeBook;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }
    /**
     * Draws player information on the screen, including crit chance, crit damage, and damage reduction.
     *
     * @param context  The drawing context.
     * @param mouseX   The x-coordinate of the mouse.
     * @param mouseY   The y-coordinate of the mouse.
     */
    @Inject(method = "drawBackground", at = @At(value = "RETURN"))
    public void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            drawPlayerInfo(context, player, mouseX, mouseY);
        }
    }
    /**
     * Draws player information, including crit chance, crit damage, and damage reduction.
     *
     * @param context  The drawing context.
     * @param player   The client player entity.
     * @param mouseX   The x-coordinate of the mouse.
     * @param mouseY   The y-coordinate of the mouse.
     */
    @Unique
    private void drawPlayerInfo(DrawContext context, ClientPlayerEntity player, int mouseX, int mouseY) {
        int a = ModEntityComponents.STATS.get(player).getAccuracy();
        int b = ModEntityComponents.STATS.get(player).getEvasion();
        MutableText[] information = new MutableText[]{
                Text.translatable(PLAYER_INFO_KEY + "header"),
                Text.translatable(PLAYER_INFO_KEY + "accuracy").append(String.format(": %d", a)),
                Text.translatable(PLAYER_INFO_KEY + "evasion").append(String.format(": %d", b)),
        };

        if (!recipeBook.isOpen()) {
            int i = this.x - 128;
            int j = this.y;

            context.drawTexture(PLAYERINFO_TEXTURE, i, j, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);

            int yOffset = 10;
            int xOffset = 35;

            for (int index = 0; index < information.length; index++) {
                MutableText text = information[index];

                int offset = (index == 0) ? 24 : 15;
                int xoffset2 = 10;

                context.drawText(textRenderer, text.getString(), i + xOffset, j + yOffset, 0xFFFFFF, false);

                if (index != 0 && isMouseOverText(mouseX, mouseY, i + xOffset, j + yOffset, textRenderer.getWidth(text), 10)) {
                    Map<Integer, String> tooltipKeyMap = Map.of(
                            1, "accuracy",
                            2, "evasion",
                            3, ""
                    );
                    String tooltipKey = tooltipKeyMap.getOrDefault(index, "unknown"); // Default to "unknown" if index is not found

                    List<Text> tooltipText = new ArrayList<>();
                    tooltipText.add(Text.translatable(PLAYER_INFO_KEY + "tooltip." + tooltipKey));

                    int x = mouseX - 5;
                    int y = mouseY + 2;
                    context.drawTooltip(textRenderer, tooltipText.getFirst(), x, y);
                }
                yOffset += offset;
                xOffset = xoffset2;
            }
        }
    }

    @Unique
    private boolean isMouseOverText(int mouseX, int mouseY, int textX, int textY, int textWidth, int textHeight) {
        return mouseX >= textX && mouseX <= textX + textWidth && mouseY >= textY && mouseY <= textY + textHeight;
    }
}