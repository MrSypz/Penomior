package sypztep.penomior.common.init;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import sypztep.penomior.Penomior;

public class ModEntityAttributes {
    public static final RegistryEntry<EntityAttribute> GENERIC_CRIT_DAMAGE = register("generic.crit_damage", new ClampedEntityAttribute("attribute.name.generic.crit_damage", 1.5, 0.0, 64.0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_CRIT_CHANCE = register("generic.crit_chance", new ClampedEntityAttribute("attribute.name.generic.crit_chance", 0, 0.0, 1.0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_HEALTH_REGEN = register("generic.health_regen", new ClampedEntityAttribute("attribute.name.generic.health_regen", 0, 0.0, 16).setTracked(true));

    public static final RegistryEntry<EntityAttribute> GENERIC_MAGIC_RESISTANCE = register("generic.magic_resistance", new ClampedEntityAttribute("attribute.name.generic.magic_resistance", 0, -2.0, 2.0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_PHYSICAL_RESISTANCE = register("generic.physical_resistance", new ClampedEntityAttribute("attribute.name.generic.physical_resistance", 0, -2.0, 0.8).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_PROJECTILE_RESISTANCE = register("generic.projectile_resistance", new ClampedEntityAttribute("attribute.name.generic.projectile_resistance", 0, -2.0, 0.8).setTracked(true));

    public static final RegistryEntry<EntityAttribute> GENERIC_MAGIC_ATTACK_DAMAGE = register("generic.magic_attack_damage", new ClampedEntityAttribute("attribute.name.generic.magic_attack_damage", 0, 0.0, 1024.0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_MELEE_ATTACK_DAMAGE = register("generic.melee_attack_damage", new ClampedEntityAttribute("attribute.name.generic.melee_attack_damage", 0, 0.0, 1024.0).setTracked(true));
    public static final RegistryEntry<EntityAttribute> GENERIC_PROJECTILE_ATTACK_DAMAGE = register("generic.projectile_attack_damage", new ClampedEntityAttribute("attribute.name.generic.projectile_attack_damage", 0, 0.0, 1024.0).setTracked(true));


    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Penomior.id(id), attribute);
    }
}
