package sypztep.test;

import net.minecraft.util.math.MathHelper;

public class newformula {
    public static float getDamageLeft(float damageAmount, float armor, float armorToughness) {
        float i;
        float f = 2.0f + armorToughness / 4.0f;
        float g = MathHelper.clamp(armor - damageAmount / f, armor * 0.2f, 20.0f);
        float h = g / 25.0f;
//        i = -0.6f;
        float j = 1.0f - h;
        return damageAmount * j;
    }
    public static float calculateDamageLeft(float damageAmount, float armorPoints, float flatDR, float armorPenetration) {
        // Step 1: Apply armor penetration to calculate effective armor
        float effectiveArmor = armorPoints * (1 - armorPenetration);

        // Step 2: Calculate damage reduction from effective armor
        float armorReduction = effectiveArmor / (effectiveArmor + 100); // Example formula for armor reduction scaling
        float damageAfterArmor = damageAmount * (1 - armorReduction);

        // Step 3: Apply flat damage reduction (DR) after armor reduction
        float damageAfterFlatDR = damageAfterArmor - flatDR / 2f;

        // Ensure damage doesn't go below zero after flat DR
        damageAfterFlatDR = Math.max(damageAfterFlatDR, 0);

        // Return the final damage
        return damageAfterFlatDR;
    }

    public static void main(String[] args) {
        float damage = 10;
        float armor = 20;
        float armortoughness = 12;
        System.out.println("-Old Damage formula-");
        System.out.println(getDamageLeft(damage,armor,armortoughness));
        float finalDamage = calculateDamageLeft(damage, armor, armortoughness, 0.0f);
        System.out.println("-New formula-");

        System.out.println("Final damage taken: " + finalDamage);
    }
}

