package sypztep.penomior.mixin.qol.statwidget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.penomior.Penomior;
import sypztep.penomior.client.widget.TabWidgetButton;
import sypztep.penomior.client.widget.tab.RefineButtonWidget;
import sypztep.penomior.client.widget.tab.StatButtonWidget;

@Mixin(InventoryScreen.class)
@Environment(EnvType.CLIENT)
public abstract class InventoryScreenMixin extends HandledScreen<PlayerScreenHandler> {
    @Unique
    private TabWidgetButton[] tabButtons;


    public InventoryScreenMixin(PlayerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        int width = 32;
        int height = 26;

        TabWidgetButton refineTab = new RefineButtonWidget(0, 0, width, height, Text.literal("refine"), this, Penomior.id("hud/container/tab/icon/refine"));
        TabWidgetButton statTab = new StatButtonWidget(0, 0, width, height, Text.literal("stats"), this.client, this, Identifier.ofVanilla("icon/accessibility"));

        tabButtons = new TabWidgetButton[] { refineTab, statTab };

        setButtonCoordinates();

        for (TabWidgetButton button : tabButtons) {
            this.addDrawableChild(button);
        }
    }


    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (tabButtons != null) {
            for (TabWidgetButton button : tabButtons) {
                button.render(context, mouseX, mouseY, delta);
            }
        }
    }


    @Unique
    private void setButtonCoordinates() {
        int baseY = (this.height / 4);

        for (int index = 0; index < tabButtons.length; index++) {
            TabWidgetButton button = tabButtons[index];
            button.setX(this.x - 31);
            button.setY(baseY + index * 27);
        }
    }
}
