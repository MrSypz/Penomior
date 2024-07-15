package sypztep.penomior.mixin.vanilla.evasionaccuracy;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.penomior.ModConfig;
import sypztep.penomior.client.payload.AddMissingParticlesPayload;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.CombatUtils;
import sypztep.penomior.common.util.RefineUtil;

import java.util.*;
import java.util.List;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;wakeUp()V", shift = At.Shift.BY, by = 2), cancellable = true)
    private void handleMissing(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity target = (LivingEntity) (Object) this;
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingAttacker) {
            StatsComponent targetStats = ModEntityComponents.STATS.getNullable(target); // who take damage
            StatsComponent attackerStats = ModEntityComponents.STATS.getNullable(livingAttacker); // who attack?

            if (targetStats == null || attackerStats == null) return;

            boolean isMissing = CombatUtils.isMissingHits(attackerStats, targetStats);
            Identifier identifier = EntityType.getId(livingAttacker.getType());
            for (String id : ModConfig.dmgReceiveExcludedEntities) {
                if (identifier != null && identifier.toString().contains(id)) {
                    return;
                }
            }
            if (isMissing) {// missing attack
                if (livingAttacker.squaredDistanceTo(target) < 2500) {
                    PlayerLookup.tracking(this).forEach(foundPlayer -> AddMissingParticlesPayload.send(foundPlayer, this.getId()));
                    if (livingAttacker instanceof PlayerEntity)
                        PlayerLookup.tracking(livingAttacker).forEach(foundPlayer -> AddMissingParticlesPayload.send(foundPlayer, this.getId()));
                }
                cir.setReturnValue(false); // change from ci.cancle() to cancle
            }
        }
    }

    @Inject(method = "getEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;applyAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void LivingEntityOnEquipmentChange(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        MutableInt evasion = new MutableInt();
        MutableInt accuracy = new MutableInt();
        List<NbtCompound> equippedNbt = RefineUtil.getNbtFromAllEquippedSlots((LivingEntity) (Object) this);
        for (NbtCompound nbt : equippedNbt) {
            evasion.add(nbt.getInt(PenomiorData.EVASION));
            accuracy.add(nbt.getInt(PenomiorData.ACCURACY));
        }
        ModEntityComponents.STATS.get(this).setEvasion(evasion.intValue());
        ModEntityComponents.STATS.get(this).setAccuracy(accuracy.intValue());
    }
}
