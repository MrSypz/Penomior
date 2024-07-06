package sypztep.penomior.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.penomior.client.particle.TextParticle;
import sypztep.penomior.common.component.StatsComponent;
import sypztep.penomior.common.data.PenomiorData;
import sypztep.penomior.common.init.ModDataComponents;
import sypztep.penomior.common.init.ModEntityComponents;
import sypztep.penomior.common.util.CombatUtils;
import sypztep.tyrannus.common.util.ItemStackHelper;

import java.util.*;
import java.util.List;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Unique
    private final Deque<TextParticle> particles = new ArrayDeque<>();

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;wakeUp()V", shift = At.Shift.BY, by = 2), cancellable = true)
    private void applyDamageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        var client = MinecraftClient.getInstance();
        var world = client.world;
        if (world == null || !world.isClient()) return;

        // this in this method is target
        if (attacker instanceof LivingEntity living) {
            StatsComponent targetStats = ModEntityComponents.STATS.getNullable(this);
            StatsComponent attackerStats = ModEntityComponents.STATS.getNullable(living);

            if (targetStats == null || attackerStats == null) return;

            int attackerAccuracy = attackerStats.getAccuracy();
            int targetEvasion = targetStats.getEvasion();
            double hitrate = CombatUtils.calculateHitRate(attackerAccuracy, targetEvasion);

            System.out.println("Attacker Accuracy: " + attackerAccuracy);
            System.out.println("Target Evasion: " + targetEvasion);
            System.out.println("Hit chance: " + hitrate);

            // Here you can decide how to use these rates to determine the outcome of an attack
            // For example, you might roll a random number to see if the attack hits
            Random random = new Random();
            boolean attackHits = random.nextDouble() < hitrate * 0.01f; // Dividing by 100 to convert percentage to a decimal
            Vec3d particlePos = this.getPos().add(0.0, this.getHeight() + 0.25, 0.0);
            Vec3d particleVelocity = this.getVelocity();
            Vec3d particleVelocityForward = this.getPos();
            particleVelocityForward = particleVelocityForward.subtract(client.gameRenderer.getCamera().getPos()).normalize();
            particleVelocityForward = particleVelocityForward.multiply(this.getWidth() * 10.0);
            particleVelocity = particleVelocity.subtract(particleVelocityForward.x, -20.0, particleVelocityForward.z);

            var particle = new TextParticle(world, particlePos, particleVelocity);


            if (attackHits) {
                var text = String.format("%.1f", amount);
                if (text.endsWith(".0")) {
                    text = text.substring(0, text.length() - 2);
                }
                particle.setText(text);
                System.out.println("Attack hits!");
            } else {
                System.out.println("Attack misses!");
                particle.setText("Miss");
                cir.setReturnValue(false);
                // Handle miss here
            }
            particles.add(particle);
            client.particleManager.addParticle(particle);
        }
    }

    @Unique
    public List<NbtCompound> getNbtFromAllEquippedSlots() {
        List<NbtCompound> nbtList = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = this.getEquippedStack(slot);
            if (!itemStack.isEmpty())
                nbtList.add(ItemStackHelper.getNbtCompound(itemStack, ModDataComponents.PENOMIOR));
        }

        return nbtList;
    }

    @Inject(method = "getEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;applyAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;Ljava/util/function/BiConsumer;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void LivingEntityOnEquipmentChange(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir) {
        MutableInt extraHealth = new MutableInt();
        List<NbtCompound> equippedNbt = getNbtFromAllEquippedSlots();
        for (NbtCompound nbt : equippedNbt)
            extraHealth.add(nbt.getFloat(PenomiorData.EVASION));
        ModEntityComponents.STATS.get(this).setEvasion(extraHealth.intValue());
    }
}
