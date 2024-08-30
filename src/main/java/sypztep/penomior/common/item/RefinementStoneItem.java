package sypztep.penomior.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class RefinementStoneItem extends Item {
    private final String desc;
    public RefinementStoneItem(Settings settings,String desc) {
        super(settings);
        this.desc = desc;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.literal(desc).formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
