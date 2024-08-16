package sypztep.penomior.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import sypztep.penomior.common.init.ModEntityComponents;

public class SetPointCommand implements CommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("stats")
                .then(CommandManager.literal("set").requires(source -> source.hasPermissionLevel(3))  // Requires permission level 2
                        .then(CommandManager.argument("level", IntegerArgumentType.integer())
                                .executes(context -> execute(context, IntegerArgumentType.getInteger(context, "level")))))
                .then(CommandManager.literal("reset").requires(source -> source.hasPermissionLevel(3))
                        .executes(context -> reset(context))));
    }

    private static int execute(CommandContext<ServerCommandSource> context,int amount) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        var playerStats = ModEntityComponents.UNIQUESTATS.get(player);
            playerStats.getPlayerStats().getLevelSystem().setStatPoints(amount);
            playerStats.sync();

        return 1;
    }
    private static int reset(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        var playerStats = ModEntityComponents.UNIQUESTATS.get(player);
            playerStats.getPlayerStats().resetStats(player);
            playerStats.sync();
        return 1;
    }
}
