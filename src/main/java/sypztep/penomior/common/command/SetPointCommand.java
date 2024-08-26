package sypztep.penomior.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import sypztep.penomior.common.init.ModEntityComponents;

public class SetPointCommand implements CommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("stats")
                .then(CommandManager.literal("set").requires(source -> source.hasPermissionLevel(3))  // Requires permission level 2
                        .then(CommandManager.argument("level", IntegerArgumentType.integer())
                                .executes(context -> execute(context, IntegerArgumentType.getInteger(context, "level")))))
                .then(CommandManager.literal("reset").requires(source -> source.hasPermissionLevel(3))
                        .executes(SetPointCommand::reset)));
    }

    private static int execute(CommandContext<ServerCommandSource> context, int amount) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        var playerStats = ModEntityComponents.UNIQUESTATS.get(player);
        playerStats.getPlayerStats().getLevelSystem().setStatPoints(amount);
        playerStats.sync();

        player.sendMessage(Text.literal("Stat points set to " + amount), false);

        return 1;
    }

    private static int reset(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        var playerStats = ModEntityComponents.UNIQUESTATS.get(player);
        var playerStatsCombat = ModEntityComponents.STATS.get(player);
        playerStats.getPlayerStats().resetStats(player);
        playerStats.sync();
        playerStatsCombat.resetExtras();

        player.sendMessage(Text.literal("Stats have been reset."), false);
        return 1;
    }
}
