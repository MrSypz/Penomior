package sypztep.penomior.common.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.penomior.Penomior;
import sypztep.penomior.common.block.RefineTable;

public class ModBlockItem {
    public static final Block REFINE_TABLE = new RefineTable(AbstractBlock.Settings.copy(Blocks.SMITHING_TABLE));
    public static void init() {
        Registry.register(Registries.BLOCK, Penomior.id("refine_table"), REFINE_TABLE);
    }
}
