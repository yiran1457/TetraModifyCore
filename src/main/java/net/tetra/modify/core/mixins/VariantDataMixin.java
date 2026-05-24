package net.tetra.modify.core.mixins;

import net.tetra.modify.core.IMaterialDataProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.data.VariantData;

@Mixin(value = VariantData.class,remap = false)
public class VariantDataMixin implements IMaterialDataProvider {
    public transient MaterialData materialData;

    @Override
    public @Nullable MaterialData getMaterialData() {
        return materialData;
    }

    @Override
    public void setMaterialData(MaterialData materialData) {
        this.materialData = materialData;
    }
}
