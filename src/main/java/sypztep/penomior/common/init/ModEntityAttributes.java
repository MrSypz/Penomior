package sypztep.penomior.common.init;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class ModEntityAttributes {
    public static final RegistryEntry<EntityAttribute> GENERIC_CRIT_DAMAGE = register("generic.crit_damage", new ClampedEntityAttribute("attribute.name.generic.crit_damage", 1.5, 0.0, 64.0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_CRIT_CHANCE = register("generic.crit_chance", new ClampedEntityAttribute("attribute.name.generic.crit_chance", 0, 0.0, 1.0).setTracked(true));

    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.ofVanilla(id), attribute);
    }
}
