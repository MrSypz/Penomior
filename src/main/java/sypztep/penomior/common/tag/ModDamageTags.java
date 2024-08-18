package sypztep.penomior.common.tag;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import sypztep.penomior.Penomior;

public class ModDamageTags {
    public static final TagKey<DamageType> PHYSICAL_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, Penomior.id("physical_damage"));
    public static final TagKey<DamageType> MELEE_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, Penomior.id("melee_damage"));
    public static final TagKey<DamageType> MAGIC_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, Penomior.id("magic_damage"));
    public static final TagKey<DamageType> FIRE_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, Penomior.id("fire_damage"));
    public static final TagKey<DamageType> PROJECTILE_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, Penomior.id("projectile_damage"));
}
