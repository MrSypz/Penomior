package sypztep.penomior.common.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import sypztep.penomior.client.payload.RefinePayloadS2C;
import sypztep.penomior.common.data.PenomiorItemData;
import sypztep.penomior.common.data.PenomiorItemDataSerializer;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.init.ModItem;
import sypztep.penomior.common.init.ModScreenHandler;
import sypztep.penomior.common.util.RefineUtil;
//TODO : make a failstacks system and pri duo tet enchant
public class RefineScreenHandler extends ScreenHandler {
    private final Inventory inventory = new SimpleInventory(3) {
        @Override
        public void markDirty() {
            super.markDirty();
            RefineScreenHandler.this.onContentChanged(this);
        }
    };
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private boolean canRefine;
    private BlockPos pos;

    public RefineScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandler.GRINDER_SCREEN_HANDLER_TYPE, syncId);

        this.context = context;
        this.player = playerInventory.player;
        addSlot(new Slot(this.inventory, 0, 9, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isRefineMaterial(stack);
            }
        });
        addSlot(new Slot(this.inventory, 1, 151, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return matchesItemData(stack);
            }
        });
        addSlot(new Slot(this.inventory, 2, 29, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });
        int i;
        for (i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));

        this.context.run((world, pos) -> RefineScreenHandler.this.setPos(pos));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (!player.getWorld().isClient() && inventory == this.inventory) {
            this.doRefineTask();
        }
    }

    private void doRefineTask() {
        ItemStack slotOutput = this.getSlot(1).getStack();

        if (RefineUtil.getRefineLvl(slotOutput) < 20) {
            this.canRefine = false;
        }
        if (this.getSlot(0).hasStack() && this.getSlot(1).hasStack()) {
            ItemStack material = this.getSlot(0).getStack();
            if (matchesItemData(slotOutput)) {
                boolean isArmor = slotOutput.getItem() instanceof ArmorItem;
                boolean isRefined = slotOutput.get(ModDataComponents.PENOMIOR) != null;

                if (isRefined && !isArmor) {
                    this.canRefine = material.isOf(ModItem.REFINE_WEAPON_STONE);
                } else if (isRefined) {
                    this.canRefine = material.isOf(ModItem.REFINE_ARMOR_STONE);
                }
            }
        } else {
            this.canRefine = false;
        }
        RefinePayloadS2C.send((ServerPlayerEntity) player, !this.canRefine);
    }


    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) -> player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 64.0, true);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        //TODO : make it quick moveble
//        if (slot != null && slot.hasStack()) {
//            ItemStack slotStack = slot.getStack();
//            stack = slotStack.copy();
//
//         If the slot clicked is one of the container's slots
//            if (index < 3) { // 0, 1, 2 are container slots
//                if (!insertItem(slotStack, 3, 39, true)) { // Player inventory slots: 3 to 38 (hotbar included)
//                    return ItemStack.EMPTY;
//                }
//                slot.onQuickTransfer(slotStack, stack);
//            } else {
//                // If the slot clicked is one of the player inventory slots
//                if (isGrinderMaterial(slotStack)) {
//                    if (!insertItem(slotStack, 0, 1, false)) {
//                        return ItemStack.EMPTY;
//                    }
//                } else if (isGrindableItem(slotStack)) {
//                    if (!insertItem(slotStack, 1, 2, false)) {
//                        return ItemStack.EMPTY;
//                    }
//                } else if (slotStack.isOf(Items.COPPER_INGOT)) {
//                    if (!insertItem(slotStack, 2, 3, false)) {
//                        return ItemStack.EMPTY;
//                    }
//                } else {
//                    if (index >= 3 && index < 30) { // Player main inventory (excluding hotbar)
//                        if (!insertItem(slotStack, 30, 39, false)) { // Try hotbar
//                            return ItemStack.EMPTY;
//                        }
//                    } else if (index >= 30 && index < 39) { // Hotbar
//                        if (!insertItem(slotStack, 3, 30, false)) { // Try main inventory
//                            return ItemStack.EMPTY;
//                        }
//                    } else {
//                        return ItemStack.EMPTY;
//                    }
//                }
//            }
//
//            if (slotStack.isEmpty()) {
//                slot.setStack(ItemStack.EMPTY);
//            } else {
//                slot.markDirty();
//            }
//
//            if (slotStack.getCount() == stack.getCount()) {
//                return ItemStack.EMPTY;
//            }
//            slot.onTakeItem(player, slotStack);
//        }
        return stack;
    }

    private boolean matchesItemData(ItemStack stack) {
        String itemID = Registries.ITEM.getId(stack.getItem()).toString();
        PenomiorItemData itemData = PenomiorItemDataSerializer.getConfigCache().get(itemID);
        return itemData != null && itemID.equals(itemData.itemID());
    }

    private boolean isRefineMaterial(ItemStack stack) {
        return stack.isOf(ModItem.REFINE_WEAPON_STONE) || stack.isOf(ModItem.REFINE_ARMOR_STONE);
    }

    public void refine() {
        ItemStack slotOutput = this.getSlot(1).getStack();
        if (matchesItemData(slotOutput) && RefineUtil.getRefineLvl(slotOutput) < 20) {
            boolean isArmor = slotOutput.getItem() instanceof ArmorItem;
            boolean isRefined = slotOutput.get(ModDataComponents.PENOMIOR) != null;
            PenomiorItemData itemData = PenomiorItemData.getPenomiroItemData(slotOutput);
            int maxLvl = itemData.maxLvl();
            int startAccuracy = itemData.startAccuracy();
            int endAccuracy = itemData.endAccuracy();
            int startEvasion = itemData.startEvasion();
            int endEvasion = itemData.endEvasion();


            if (isRefined && !isArmor) {
                int currentRefineLvl = RefineUtil.getRefineLvl(slotOutput);
                RefineUtil.setRefineLvl(slotOutput, currentRefineLvl + 1);

                RefineUtil.setAccuracy(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startAccuracy, endAccuracy);
            } else if (isRefined) {
                int currentRefineLvl = RefineUtil.getRefineLvl(slotOutput);
                RefineUtil.setRefineLvl(slotOutput, currentRefineLvl + 1);

                RefineUtil.setEvasion(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startEvasion, endEvasion);
                RefineUtil.setAccuracy(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startAccuracy, endAccuracy);
            }
            this.decrementStack(0);
            this.context.run((world, pos) -> world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, pos, 0));
        }
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.inventory.getStack(slot);
        itemStack.decrement(1);
        this.inventory.setStack(slot, itemStack);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.inventory && super.canInsertIntoSlot(stack, slot);
    }
}
