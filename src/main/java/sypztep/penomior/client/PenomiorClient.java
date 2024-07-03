package sypztep.penomior.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;
import sypztep.penomior.common.util.RefineUtil;


public class PenomiorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
                lines.add(Text.of( "Accuracy"+ String.valueOf(RefineUtil.getAccuracy(stack))));
                lines.add(Text.of("Evasion"+ String.valueOf(RefineUtil.getEvasion(stack))));
        });
    }
}
