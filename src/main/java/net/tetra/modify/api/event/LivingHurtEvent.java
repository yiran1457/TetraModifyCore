package net.tetra.modify.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class LivingHurtEvent extends LivingEvent {
    private final DamageSource source;
    private final float originalAmount;
    private float baseAmount = 0;
    private float extraAmount = 0;
    private float multiplyBaseAmount = 1;
    private float multiplyTotalAmount = 1;

    public LivingHurtEvent(LivingEntity entity, DamageSource source, float amount) {
        super(entity);
        this.source = source;
        this.originalAmount = amount;
    }


    public DamageSource getSource() {
        return this.source;
    }

    public float getOriginalAmount() {
        return this.originalAmount;
    }

    public void addBaseAmount(float amount) {
        this.baseAmount += amount;
    }

    public float getBaseAmount() {
        return this.baseAmount;
    }

    public void addExtraAmount(float amount) {
        this.extraAmount += amount;
    }

    public float getExtraAmount() {
        return this.extraAmount;
    }

    public void addMultiplyBaseAmount(float amount) {
        multiplyBaseAmount += amount;
    }

    public float getMultiplyBaseAmount() {
        return this.multiplyBaseAmount;
    }

    public void addMultiplyTotalAmount(float amount) {
        multiplyTotalAmount *= amount;
    }

    public float getMultiplyTotalAmount() {
        return this.multiplyTotalAmount;
    }

    public float getResultAmount() {
        return (this.originalAmount + this.baseAmount) * this.multiplyBaseAmount * this.multiplyTotalAmount + this.extraAmount;
    }

    public static class Pre extends LivingHurtEvent {

        public Pre(LivingEntity entity, DamageSource source, float amount) {
            super(entity, source, amount);
        }
    }

    public static class Post extends LivingHurtEvent {

        public Post(LivingEntity entity, DamageSource source, float amount) {
            super(entity, source, amount);
        }
    }
}