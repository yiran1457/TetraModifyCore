package net.tetra.modify.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.ItemStack;
import net.tetra.modify.utils.MatchUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchScreen;

@Mixin(WorkbenchScreen.class)
public class WorkbenchScreenMixin {
    @WrapOperation(method = "onTileEntityChange",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",ordinal = 0))
    public boolean onTileEntityChange(ItemStack stack, ItemStack other, Operation<Boolean> original) {
        return MatchUtils.matchItem(stack, other);
    }
}
