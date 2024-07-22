package sypztep.penomior.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import org.jetbrains.annotations.Nullable;
import sypztep.penomior.common.screen.RefineScreenHandler;

import static net.minecraft.server.command.CommandManager.literal;

public class RefineCommand implements CommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("refine")
                .then(CommandManager.literal("open")
                .executes(RefineCommand::execute)));
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player != null) {
            player.openHandledScreen(createScreenHandlerFactory(player.getWorld(), player.getBlockPos()));
        }

        return 1;
    }

    @Nullable
    protected static NamedScreenHandlerFactory createScreenHandlerFactory(World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new RefineScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), Text.of("refiner"));
    }
}
