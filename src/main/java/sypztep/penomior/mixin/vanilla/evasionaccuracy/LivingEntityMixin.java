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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.penomior.client.payload.AddMissingParticlesPayload;
import sypztep.penomior.common.util.interfaces.MissingAccessor;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.CombatUtils;
import sypztep.penomior.common.util.RefineUtil;

import java.util.*;
import java.util.List;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements MissingAccessor {
    @Unique
    boolean isMissing;
    @Unique
    LivingEntity target = (LivingEntity) (Object) this;

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSleeping()Z", ordinal = 0), cancellable = true)
    private void handleMissing(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingAttacker) {
            StatsComponent targetStats = ModEntityComponents.STATS.getNullable(target); // who take damage
            StatsComponent attackerStats = ModEntityComponents.STATS.getNullable(livingAttacker); // who attack?

            if (targetStats == null || attackerStats == null) return;

            isMissing = CombatUtils.isMissingHits(attackerStats, targetStats);
            penomior$setMissing(isMissing);

            if (isMissing) {// missing attack
                if (livingAttacker.squaredDistanceTo(target) < 1500) {
                    PlayerLookup.tracking((ServerWorld) target.getWorld(), target.getChunkPos()).forEach(foundPlayer -> AddMissingParticlesPayload.send(foundPlayer, this.getId())); //Who Take Damage
                    PlayerLookup.tracking((ServerWorld) livingAttacker.getWorld(), livingAttacker.getChunkPos()).forEach(foundPlayer -> AddMissingParticlesPayload.send(foundPlayer, this.getId())); // Attacker
                }
                cir.setReturnValue(false); // change from ci.cancle() to cancle
            }
        }
    }

    @Inject(method = "getEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;applyAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void LivingEntityOnEquipmentChange(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        if (target instanceof PlayerEntity) {
            MutableInt evasion = new MutableInt();
            MutableInt accuracy = new MutableInt();
            List<NbtCompound> equippedNbt = RefineUtil.getNbtFromAllEquippedSlots(target);
            for (NbtCompound nbt : equippedNbt) {
                evasion.add(nbt.getInt(PenomiorData.EVASION));
                accuracy.add(nbt.getInt(PenomiorData.ACCURACY));
            }
            ModEntityComponents.STATS.get(target).setEvasion(evasion.intValue());
            ModEntityComponents.STATS.get(target).setAccuracy(accuracy.intValue());
        }
    }

    @Override
    public boolean penomior$isMissing() {
        return isMissing;
    }

    @Override
    public void penomior$setMissing(boolean missing) {
        isMissing = missing;
    }
}
