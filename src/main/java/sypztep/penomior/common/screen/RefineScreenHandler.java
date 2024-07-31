package sypztep.penomior.common.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.penomior.client.payload.AddRefineSoundPayloadS2C;
import sypztep.penomior.client.payload.RefinePayloadS2C;
import sypztep.penomior.common.data.PenomiorItemEntry;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.init.ModItems;
import sypztep.penomior.common.init.ModScreenHandler;
import sypztep.penomior.common.util.RefineUtil;

import java.util.NoSuchElementException;
import java.util.Optional;

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

    public PlayerEntity getPlayer() {
        return player;
    }

    public RefineScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ModScreenHandler.REFINE_SCREEN_HANDLER_TYPE, syncId);

        this.context = context;
        this.player = playerInventory.player;
        addSlot(new Slot(this.inventory, 0, 31, 30) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isRefineMaterial(stack);
            }
        });
        addSlot(new Slot(this.inventory, 1, 143, 30) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isValidItem(stack);
            }
        });
        addSlot(new Slot(this.inventory, 2, 11, 49) {
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

        boolean allSlotInsert = this.getSlot(0).hasStack() && this.getSlot(1).hasStack();
        boolean canRefine = false;

        if (allSlotInsert && isValidItem(slotOutput) && RefineUtil.getRefineLvl(slotOutput) < 20) {
            ItemStack material = this.getSlot(0).getStack();
            boolean isArmor = slotOutput.getItem() instanceof ArmorItem;
            boolean isRefined = slotOutput.get(ModDataComponents.PENOMIOR) != null;
            int currentRefineLvl = RefineUtil.getRefineLvl(slotOutput);

            // Determine if refinement is possible based on item type and material
            if (!isRefined && !isArmor) {
                canRefine = material.isOf(ModItems.REFINE_WEAPON_STONE) && currentRefineLvl < 14;
            } else if (!isRefined) {
                canRefine = material.isOf(ModItems.REFINE_ARMOR_STONE) && currentRefineLvl < 14;
            } else if (!isArmor) {
                canRefine = material.isOf(ModItems.REFINE_WEAPON_STONE) && currentRefineLvl < 14;
            } else {
                canRefine = material.isOf(ModItems.REFINE_ARMOR_STONE) && currentRefineLvl < 14;
            }
            if (material.isOf(ModItems.MOONLIGHT_CRESCENT)) {
                canRefine = true;
            }
        }
        int failStack = ModEntityComponents.STATS.get(this.player).getFailstack();
        RefineUtil.getCalculateSuccessRate(slotOutput, failStack);
        RefinePayloadS2C.send((ServerPlayerEntity) this.player, !canRefine);
    }


    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public boolean isValidItem(ItemStack stack) {
        String itemID = PenomiorItemEntry.getItemId(stack);
        Optional<PenomiorItemEntry> itemDataOpt = PenomiorItemEntry.getPenomiorItemData(itemID);
        return itemDataOpt.isPresent();
    }

    private boolean isRefineMaterial(ItemStack stack) {
        return stack.isOf(ModItems.REFINE_WEAPON_STONE) || stack.isOf(ModItems.REFINE_ARMOR_STONE) || stack.isOf(ModItems.MOONLIGHT_CRESCENT);
    }

    public void refine() {
        ItemStack slotOutput = this.getSlot(1).getStack();
        String itemID = PenomiorItemEntry.getItemId(slotOutput);
        Optional<PenomiorItemEntry> itemDataOpt = PenomiorItemEntry.getPenomiorItemData(itemID);
        PenomiorItemEntry itemData = PenomiorItemEntry.getPenomiorItemData(itemID)
                .orElseThrow(() -> new NoSuchElementException("Item data not found for item ID: " + itemID));
        if (itemDataOpt.isPresent())
            itemData = itemDataOpt.get();

        RefineUtil.initializeItemData(slotOutput, itemData); // this one will excute when data is null
        //----------pre define-----------//
        int currentRefineLvl = RefineUtil.getRefineLvl(slotOutput);
        int maxLvl = itemData.maxLvl();
        int startAccuracy = itemData.startAccuracy();
        int endAccuracy = itemData.endAccuracy();
        int startEvasion = itemData.startEvasion();
        int endEvasion = itemData.endEvasion();
        int durability = RefineUtil.getDurability(slotOutput);
        int startDamage = itemData.starDamage();
        int endDamage = itemData.endDamage();
        int startProtect = itemData.startProtection();
        int endProtect = itemData.endProtection();
        int failStack = ModEntityComponents.STATS.get(this.player).getFailstack();
        int repairPoint = itemData.repairpoint();
        ItemStack material = this.getSlot(0).getStack();
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        if (isValidItem(slotOutput) && RefineUtil.getRefineLvl(slotOutput) < itemData.maxLvl() && durability > 0 && !material.isOf(ModItems.MOONLIGHT_CRESCENT)) {
            // Refinement process
            if (RefineUtil.handleRefine(slotOutput, failStack)) { // Random Success Rate
                RefineUtil.setRefineLvl(slotOutput, currentRefineLvl + 1);
                RefineUtil.setEvasion(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startEvasion, endEvasion);
                RefineUtil.setAccuracy(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startAccuracy, endAccuracy);
                RefineUtil.setExtraDamage(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startDamage, endDamage);
                RefineUtil.setExtraProtect(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startProtect, endProtect);
                RefineUtil.successRefine(this.player);
                AddRefineSoundPayloadS2C.send(serverPlayer, player.getId(), RefineUtil.RefineSound.SUCCESS.select());
            } else { // Fail to refine
                if (currentRefineLvl > 16) { // 17 - 20
                    RefineUtil.setRefineLvl(slotOutput, Math.max(currentRefineLvl - 1, 0));
                    RefineUtil.setEvasion(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startEvasion, endEvasion);
                    RefineUtil.setAccuracy(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startAccuracy, endAccuracy);
                    RefineUtil.setExtraDamage(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startDamage, endDamage);
                    RefineUtil.setExtraProtect(slotOutput, RefineUtil.getRefineLvl(slotOutput), maxLvl, startProtect, endProtect);
                }
                RefineUtil.setDurability(slotOutput, Math.max(RefineUtil.getDurability(slotOutput) - 10, 0));
                RefineUtil.failRefine(player, failStack);
                AddRefineSoundPayloadS2C.send(serverPlayer, player.getId(), RefineUtil.RefineSound.FAIL.select());
            }
            this.decrementStack();
            //Repair process
        } else if (material.isOf(ModItems.MOONLIGHT_CRESCENT) && durability < 100) {
            RefineUtil.setDurability(slotOutput, durability + repairPoint);
            AddRefineSoundPayloadS2C.send(serverPlayer, player.getId(), RefineUtil.RefineSound.REPAIR.select());
            this.decrementStack();
        }
    }

    private void decrementStack() {
        ItemStack itemStack = this.inventory.getStack(0);
        if (!itemStack.isEmpty()) {
            itemStack.decrement(1);
            this.inventory.setStack(0, itemStack);
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if (index < 3) { // 0, 1, 2 are container slots
                if (!insertItem(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(slotStack, stack);
            } else {
                if (isRefineMaterial(slotStack)) {
                    if (!insertItem(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isValidItem(slotStack)) {
                    if (!insertItem(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotStack.isOf(Items.COPPER_INGOT)) {
                    if (!insertItem(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (index < 30) { // Player main inventory (excluding hotbar)
                        if (!insertItem(slotStack, 30, 39, false)) { // Try hotbar
                            return ItemStack.EMPTY;
                        }
                    } else if (index < 39) { // Hotbar
                        if (!insertItem(slotStack, 3, 30, false)) { // Try main inventory
                            return ItemStack.EMPTY;
                        }
                    } else {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, slotStack);
        }
        return stack;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.inventory && super.canInsertIntoSlot(stack, slot);
    }
}
