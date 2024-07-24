package sypztep.penomior.mixin.playerinfo.client;

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
import sypztep.penomior.ModConfig;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.api.infoscreen.InfoScreenApi;
import sypztep.penomior.common.api.infoscreen.PlayerInfoProviderRegistry;
import sypztep.penomior.common.util.DrawContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sypztep.penomior.common.api.infoscreen.InfoScreenApi.*;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    @Unique
    private static final int TEXTURE_SIZE = 128;
    @Unique
    private static final Identifier PLAYERINFO_TEXTURE = Penomior.id("gui/container/player_info.png");

    @Shadow
    @Final
    private RecipeBookWidget recipeBook;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "drawBackground", at = @At(value = "RETURN"))
    public void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && ModConfig.playerstatsInfo) {
            drawPlayerInfo(context, player, mouseX, mouseY);
        }
    }

    @Unique
    private void drawPlayerInfo(DrawContext context, ClientPlayerEntity player, int mouseX, int mouseY) {
        clearInformation();

        InfoScreenApi.addInformation("header", Text.translatable(InfoScreenApi.PLAYER_INFO_KEY + "header"));

        PlayerInfoProviderRegistry.invokeProviders(new InfoScreenApi(), player);

        if (!recipeBook.isOpen()) {
            int i = this.x - 128;
            int j = this.y;

            context.drawTexture(PLAYERINFO_TEXTURE, i, j, 0, 0, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
            int yOffset = 10;
            int xOffset = 35;

            Map<String, MutableText> informationMap = InfoScreenApi.getInformation();

            for (Map.Entry<String, MutableText> entry : informationMap.entrySet()) {
                String key = entry.getKey();
                MutableText text = entry.getValue();

                int offset = key.equals("header") ? 24 : 15;

                DrawContextUtils.drawBoldText(context, textRenderer, text.getString(), i + xOffset, j + yOffset, 0xFFFFFF, 0);

                if (!key.equals("header") && isMouseOverText(mouseX, mouseY, i + xOffset, j + yOffset, textRenderer.getWidth(text), 10)) {
                    List<Text> tooltipText = new ArrayList<>();
                    tooltipText.add(Text.translatable(InfoScreenApi.PLAYER_INFO_KEY + "tooltip." + key));

                    int x = mouseX - 5;
                    int y = mouseY + 2;
                    context.drawTooltip(textRenderer, tooltipText, x, y);
                }
                yOffset += offset;
                xOffset = 10; // Reset xOffset for subsequent lines
            }
        }
    }

    @Unique
    private boolean isMouseOverText(int mouseX, int mouseY, int textX, int textY, int textWidth, int textHeight) {
        return mouseX >= textX && mouseX <= textX + textWidth && mouseY >= textY && mouseY <= textY + textHeight;
    }
}