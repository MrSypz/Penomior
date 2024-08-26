package sypztep.penomior.mixin.vanilla.agility.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import sypztep.penomior.common.init.ModEntityAttributes;

@Mixin(ModelPredicateProviderRegistry.class) @Environment(EnvType.CLIENT)
public class ModelPredicateProviderRegistryMixin {
	@ModifyReturnValue(method = "method_27890", at = @At(value = "RETURN", ordinal = 2))
	private static float modelQuickerDraw(float original) {
		ClientPlayerEntity entity = MinecraftClient.getInstance().player;
        assert entity != null;
		System.out.println(entity.getAttributeValue(ModEntityAttributes.GENERIC_PLAYER_DRAWSPEED));
        return (float) (original + entity.getAttributeValue(ModEntityAttributes.GENERIC_PLAYER_DRAWSPEED));
	}
}
