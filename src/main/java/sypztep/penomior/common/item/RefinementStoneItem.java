package sypztep.penomior.common.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RefinementStoneItem extends Item {
    private final String desc;
    public RefinementStoneItem(Settings settings,String desc) {
        super(settings);
        this.desc = desc;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.literal(desc).formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
