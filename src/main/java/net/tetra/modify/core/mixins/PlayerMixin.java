package net.tetra.modify.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.tetra.modify.api.event.LivingDamageEvent;
import net.tetra.modify.api.event.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("Duplicates")
@Mixin(Player.class)
public class PlayerMixin {
    @WrapOperation(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;onLivingHurt(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F", remap = false))
    public float hurtWrapper(LivingEntity entity, DamageSource src, float amount, Operation<Float> original) {
        LivingHurtEvent preEvent = new LivingHurtEvent.Pre(entity, src, amount);
        if (MinecraftForge.EVENT_BUS.post(preEvent)) {
            return 0.0F;
        }
        amount = original.call(entity, src, preEvent.getResultAmount());
        if (amount == 0.0F) {
            return 0.0F;
        }
        LivingHurtEvent postEvent = new LivingHurtEvent.Post(entity, src, amount);
        return MinecraftForge.EVENT_BUS.post(postEvent) ? 0.0F : postEvent.getResultAmount();
    }

    @WrapOperation(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeHooks;onLivingDamage(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F", remap = false))
    public float damageWrapper(LivingEntity entity, DamageSource src, float amount, Operation<Float> original) {
        LivingDamageEvent preEvent = new LivingDamageEvent.Pre(entity, src, amount);
        if (MinecraftForge.EVENT_BUS.post(preEvent)) {
            return 0.0F;
        }
        amount = original.call(entity, src, preEvent.getResultAmount());
        if (amount == 0.0F) {
            return 0.0F;
        }
        LivingDamageEvent postEvent = new LivingDamageEvent.Post(entity, src, amount);
        return MinecraftForge.EVENT_BUS.post(postEvent) ? 0.0F : postEvent.getResultAmount();
    }
}
