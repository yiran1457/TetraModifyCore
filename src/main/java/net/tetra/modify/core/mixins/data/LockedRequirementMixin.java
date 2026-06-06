package net.tetra.modify.core.mixins.data;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import se.mickelus.tetra.module.schematic.requirement.LockedRequirement;

import java.util.List;

@Mixin(value = LockedRequirement.class,remap = false)
public class LockedRequirementMixin {
    ResourceLocation Omniscient = ResourceLocation.parse("tetra_modify_core:omniscient");

    @WrapOperation(method = "test", at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"))
    private boolean injectOmniscient(List<ResourceLocation> instance, Object o, Operation<Boolean> original) {
        return instance.contains(Omniscient) || original.call(instance, o);
    }
}
