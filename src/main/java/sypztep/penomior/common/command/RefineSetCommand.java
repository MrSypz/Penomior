package sypztep.penomior.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import sypztep.penomior.common.data.PenomiorItemData;
import sypztep.penomior.common.util.RefineUtil;

public class RefineSetCommand implements CommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("refine")
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("lvl", IntegerArgumentType.integer())
                                .executes(context -> execute(context, IntegerArgumentType.getInteger(context, "refineLevel"))))));
    }

    private static int execute(CommandContext<ServerCommandSource> context, int lvl) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player != null) {
            ItemStack slotOutput = player.getMainHandStack();
            PenomiorItemData itemData = PenomiorItemData.getPenomiroItemData(slotOutput);

            RefineUtil.initializeItemData(slotOutput, itemData); // Ensure item data is initialized

            int maxLvl = itemData.maxLvl();

            // Validate the provided refine level
            if (lvl < 0 || lvl > maxLvl) {
                player.sendMessage(Text.literal("Invalid refine level. Must be between 0 and " + maxLvl), false);
                return 0;
            }
            // Set the refine level
            RefineUtil.setRefineLvl(slotOutput, lvl);

            // Update other attributes based on the new refine level
            int startAccuracy = itemData.startAccuracy();
            int endAccuracy = itemData.endAccuracy();
            int startEvasion = itemData.startEvasion();
            int endEvasion = itemData.endEvasion();

            RefineUtil.setEvasion(slotOutput, lvl, maxLvl, startEvasion, endEvasion);
            RefineUtil.setAccuracy(slotOutput, lvl, maxLvl, startAccuracy, endAccuracy);

            player.sendMessage(Text.literal("Refine level set to " + lvl), false);
        }

        return 1;
    }
}
