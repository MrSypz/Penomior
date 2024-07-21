package sypztep.penomior.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.init.ModItems;
import sypztep.penomior.common.payload.RefinePayloadC2S;
import sypztep.penomior.common.screen.RefineScreenHandler;
import sypztep.penomior.common.util.RefineUtil;
import sypztep.tyrannus.common.util.CyclingItemSlotIcon;

import java.util.*;

@Environment(EnvType.CLIENT)
public class RefineScreen
        extends HandledScreen<RefineScreenHandler>
        implements ScreenHandlerListener {
    public static final Identifier TEXTURE = Penomior.id("gui/container/refine_screen.png");
    public RefineButton refineButton;
    private final CyclingItemSlotIcon weaponSlotIcon = new CyclingItemSlotIcon(0);
    private final CyclingItemSlotIcon armorSlotIcon = new CyclingItemSlotIcon(0);
    private static final List<ItemStack> WEAPON_STONE = List.of(
            ModItems.REFINE_WEAPON_STONE.getDefaultStack(),
            ModItems.MOONLIGHT_CRESCENT.getDefaultStack()
            );
    private static final List<ItemStack> ARMOR_STONE = List.of(
            ModItems.REFINE_ARMOR_STONE.getDefaultStack(),
            ModItems.MOONLIGHT_CRESCENT.getDefaultStack()
    );

    public RefineScreen(RefineScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, Text.translatable(Penomior.MODID + ".refine_screen"));
        this.titleX = 60;
    }
    @Override
    protected void init() {
        super.init();
        this.handler.addListener(this);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.refineButton = this.addDrawableChild(new RefineButton(i + 80, j + 56, (button)-> {
            if (button instanceof RefineButton && !((RefineButton) button).disabled)
                RefinePayloadC2S.send();
        }));
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        this.weaponSlotIcon.updateTexture(WEAPON_STONE);
        this.armorSlotIcon.updateTexture(ARMOR_STONE);
    }

    @Override
    public void removed() {
        super.removed();
        this.handler.removeListener(this);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        RenderSystem.disableBlend();

        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        float scale = 0.75f;
        int x = (int) (32 / scale);
        int y = (int) (74 / scale);
        double successRate = RefineUtil.getSuccessRate() * 100;
        String formattedSuccessRate = String.format("%.2f%%", successRate);
        ItemStack stack = handler.getSlot(1).getStack();
        boolean bl = handler.matchesItemData(stack);
        boolean bl2 = stack.get(ModDataComponents.PENOMIOR) == null;
        if (bl) {
            context.getMatrices().push();
            context.getMatrices().scale(scale, scale, scale);
            context.drawCenteredTextWithShadow(this.textRenderer, Text.of("Rate: " + formattedSuccessRate), x + 80, y - 35, 0xE0E0E0);
            if (bl2)
                context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("???").formatted(Formatting.OBFUSCATED), 201, 78, 0xE0E0E0);
            else context.drawCenteredTextWithShadow(this.textRenderer, Text.of("Durability: " + RefineUtil.getDurability(stack)), 200, 78, 0xE0E0E0);
            context.getMatrices().pop();
        }
        context.drawCenteredTextWithShadow(this.textRenderer, Text.of("Failstack: " + ModEntityComponents.STATS.get(handler.getPlayer()).getFailstack()), 132, 10, 0xE0E0E0);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        ItemStack stack = handler.getSlot(1).getStack();
        boolean bl = handler.matchesItemData(stack);
        if (bl) {
            context.setShaderColor(1, 1, 1, 0.45F);
            if (stack.getItem() instanceof SwordItem)
                this.weaponSlotIcon.render(this.handler,context,delta,(width - backgroundWidth) / 2 + 31, (height - backgroundHeight) / 2 + 30);
            else
                this.armorSlotIcon.render(this.handler,context,delta,(width - backgroundWidth) / 2 + 31, (height - backgroundHeight) / 2 + 30);
            context.setShaderColor(1, 1, 1, 1);
        }
    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
    }

    public static class RefineButton extends ButtonWidget {
        private boolean disabled;
        @Nullable
        @Override
        public Tooltip getTooltip() {
            return Tooltip.of(Text.translatable(Penomior.MODID + ".refinebutton_tooltip"));
        }

        public RefineButton(int x, int y, PressAction onPress) {
            super(x, y, 18, 18,Text.literal("Refine"), onPress, DEFAULT_NARRATION_SUPPLIER);
            this.disabled = true;
            this.setTooltip(getTooltip());
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int v = 0;
            if (this.disabled) {
                v += this.height * 2;
            } else if (this.isHovered()) {
                v += this.height;
            }

            context.drawTexture(TEXTURE, this.getX(), this.getY(), 176, v, this.width, this.height);
        }

        public void setDisabled(boolean disable) {
            this.disabled = disable;
        }
    }
}
