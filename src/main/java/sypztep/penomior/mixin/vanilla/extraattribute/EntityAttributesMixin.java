package sypztep.penomior.mixin.vanilla.extraattribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void maxRange(String id, EntityAttribute attribute, CallbackInfoReturnable<RegistryEntry<EntityAttribute>> info) {
        if (id.equals("generic.max_health")) {
            info.setReturnValue(
                    Registry.registerReference(Registries.ATTRIBUTE, Identifier.ofVanilla(id), new ClampedEntityAttribute("attribute.name.generic.max_health", 20.0, 1.0, 8196.0).setTracked(true)));
        }
        else if (id.equals("generic.armor")) {
            info.setReturnValue(
                    Registry.registerReference(Registries.ATTRIBUTE, Identifier.ofVanilla(id), new ClampedEntityAttribute("attribute.name.generic.armor", 0.0, 0.0, 512.0).setTracked(true)));
        }
    }
}
