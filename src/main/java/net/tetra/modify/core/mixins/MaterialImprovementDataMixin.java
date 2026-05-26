package net.tetra.modify.core.mixins;

import net.tetra.modify.core.IMaterialInfoProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.module.data.ImprovementData;
import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.data.MaterialImprovementData;
import se.mickelus.tetra.module.data.MaterialMultiplier;

@Mixin(value = MaterialImprovementData.class, remap = false)
public class MaterialImprovementDataMixin {
    @Shadow
    public MaterialMultiplier extract;

    @Inject(method = "combine", at = @At("RETURN"))
    private void tmc$combine(MaterialData material, CallbackInfoReturnable<ImprovementData> cir) {
        if (cir.getReturnValue() instanceof IMaterialInfoProvider provider) {
            provider.setMaterialMultiplier(extract);
            provider.setMaterialData(material);
            provider.markHasInfo();
        }
    }
}
