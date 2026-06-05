package net.tetra.modify.core.mixins.modular;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tetra.modify.api.event.ModularGetterProjectileEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;

@Mixin(ModularBowItem.class)
public class ModularBowItemMixin {

    @WrapOperation(method = "fireArrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack tmc$fireArrow$getProjectile(Player instance, ItemStack shootable, Operation<ItemStack> original, @Local(ordinal = 0, argsOnly = true) ItemStack bowStack) {
        return ModularGetterProjectileEvent.onGetter((IModularItem) this, instance, shootable, bowStack);
    }

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getProjectile(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack tmc$use$getProjectile(Player instance, ItemStack shootable, Operation<ItemStack> original, @Local(name = "bowStack") ItemStack bowStack) {
        return ModularGetterProjectileEvent.onGetter((IModularItem) this, instance, shootable, bowStack);
    }
}
