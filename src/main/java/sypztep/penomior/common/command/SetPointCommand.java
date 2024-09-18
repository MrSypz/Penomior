package sypztep.penomior.common.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import sypztep.penomior.common.init.ModEntityComponents;

import java.util.Collection;

public class SetPointCommand implements CommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("stats")
                .then(CommandManager.literal("point")
                        .then(CommandManager.literal("set")
                                .requires(source -> source.hasPermissionLevel(3)) // Requires permission level 3
                                .then(CommandManager.argument("target", EntityArgumentType.players())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> execute(EntityArgumentType.getPlayers(context, "target"), IntegerArgumentType.getInteger(context, "amount")))))
                        ).then(CommandManager.literal("reset")
                                .requires(source -> source.hasPermissionLevel(3))
                                .then(CommandManager.argument("target", EntityArgumentType.players())
                                        .executes(context -> reset(EntityArgumentType.getPlayers(context, "target"))))
                        )
                )
                .then(CommandManager.literal("level")
                        .then(CommandManager.literal("set")
                                .requires(source -> source.hasPermissionLevel(3))
                                .then(CommandManager.argument("target", EntityArgumentType.players())
                                        .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> set(EntityArgumentType.getPlayers(context, "target"), IntegerArgumentType.getInteger(context, "amount")))))
                        )
                ));
    }

    private static int execute(Collection<ServerPlayerEntity> targets, int amount) {
        for (ServerPlayerEntity player : targets) {
            var playerStats = ModEntityComponents.UNIQUESTATS.get(player);
            playerStats.setStatPoints(amount);
            playerStats.sync();
            player.sendMessage(Text.literal("Stat points set to " + amount), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int set(Collection<ServerPlayerEntity> targets, int amount) {
        for (ServerPlayerEntity player : targets) {
            var playerStats = ModEntityComponents.UNIQUESTATS.get(player);

            var levelSystem = playerStats.getLevelSystem();
            if (amount > levelSystem.getMaxLevel() || amount < 0) {
                player.sendMessage(Text.literal(amount + " exceeds the Max Level of " + levelSystem.getMaxLevel()), false);
            } else {
                levelSystem.setLevel(amount);
                levelSystem.updateNextLvl();
                playerStats.sync();
                player.sendMessage(Text.literal("Stat points set to " + amount), false);
            }
        }
        return Command.SINGLE_SUCCESS;
    }


    private static int reset(Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            var playerStats = ModEntityComponents.UNIQUESTATS.get(player);
            var playerStatsCombat = ModEntityComponents.STATS.get(player);
            playerStats.getLivingStats().resetStats(player);
            playerStats.sync();
            playerStatsCombat.resetExtras();
            player.sendMessage(Text.literal("Stats have been reset."), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
