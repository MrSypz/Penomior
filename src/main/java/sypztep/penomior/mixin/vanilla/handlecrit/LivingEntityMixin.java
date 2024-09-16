package sypztep.penomior.mixin.vanilla.handlecrit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.penomior.common.init.ModEntityAttributes;
import sypztep.penomior.common.util.CombatUtils;
import sypztep.penomior.common.util.interfaces.NewCriticalOverhaul;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements NewCriticalOverhaul {
    @Unique
    private boolean isCrit;
    @Unique
    public boolean mobisCrit;
    @Unique
    private final Random critRandom = new Random();
    @Shadow
    public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Shadow public abstract ItemStack getStackInHand(Hand hand);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float applyDamageFirst(float amount, DamageSource source) {
        if (!this.getWorld().isClient()) {
            Entity attacker;
            attacker = source.getAttacker();

            if (attacker instanceof NewCriticalOverhaul invoker) {
                Entity projectileSource = source.getSource();
                if (projectileSource instanceof PersistentProjectileEntity) {
                    invoker.storeCrit().setCritical(this.isCritical());
                    CombatUtils.applyParticle(source.getSource());
                    return invoker.calCritDamage(amount);
                }
            }
            if (!(source.getAttacker() instanceof PlayerEntity)) {
                if (attacker instanceof NewCriticalOverhaul invoker) {
                    float critDamage = invoker.calCritDamage(amount);
                    mobisCrit = amount - critDamage != 0;
                    amount = critDamage;
                }
            }
        }
        return amount;
    }
    @Inject(method = "damage", at = @At("HEAD"))
    private void damageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul && source.getSource() instanceof PersistentProjectileEntity projectile) {
                CombatUtils.applyParticle(source.getSource());
                newCriticalOverhaul.setCritical(projectile.isCritical());
            }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void handleCrit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul) {
                if (!this.getWorld().isClient())
                    CombatUtils.applyParticle(this);
                newCriticalOverhaul.setCritical(false); //return the flag
            }
    }

    @Override
    public void setCritical(boolean setCrit) {
        if (this.getWorld().isClient()) {
            return;
        }
        this.isCrit = setCrit;
    }

    @Override
    public Random getRand() {
        return this.critRandom;
    }

    @Override
    public boolean isCritical() {
        return this.isCrit;
    }

    @Override
    public float getCritDamage() {
        return (float) this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_DAMAGE);
    }

    @Override
    public float getCritRate() {
        return (float) this.getAttributeValue(ModEntityAttributes.GENERIC_CRIT_CHANCE);
    }
}
