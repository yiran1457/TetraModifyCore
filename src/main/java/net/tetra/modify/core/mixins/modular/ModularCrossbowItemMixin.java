package net.tetra.modify.core.mixins.modular;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tetra.modify.api.event.ModularGetterProjectileEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

@Mixin(ModularCrossbowItem.class)
public class ModularCrossbowItemMixin {
    @Shadow(remap = false)
    protected ItemStack shootableDummy;

    @WrapOperation(method = "reload", at = @At(value = "INVOKE", target = "Lse/mickelus/tetra/items/modular/impl/crossbow/ModularCrossbowItem;findAmmo(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"),remap = false)
    private ItemStack tmc$reload$findAmmo(ModularCrossbowItem instance, LivingEntity entity, Operation<ItemStack> original, @Local(argsOnly = true) ItemStack crossbowStack) {
        return ModularGetterProjectileEvent.onGetter(instance, (Player) entity, shootableDummy, crossbowStack);
    }

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lse/mickelus/tetra/items/modular/impl/crossbow/ModularCrossbowItem;findAmmo(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;",remap = false))
    private ItemStack tmc$use$findAmmo(ModularCrossbowItem instance, LivingEntity entity, Operation<ItemStack> original, @Local(name = "itemstack") ItemStack itemstack) {
        return ModularGetterProjectileEvent.onGetter(instance, (Player) entity, shootableDummy, itemstack);
    }
}
