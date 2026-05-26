package net.tetra.modify.core.mixins;

import net.tetra.modify.core.IMaterialDataProvider;
import net.tetra.modify.core.IMaterialMultiplierProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.data.MaterialMultiplier;
import se.mickelus.tetra.module.data.VariantData;

@Mixin(value = VariantData.class, remap = false)
public class VariantDataMixin implements IMaterialDataProvider, IMaterialMultiplierProvider {
    public transient MaterialData materialData;
    public transient MaterialMultiplier materialMultiplier;

    @Override
    public @Nullable MaterialData getMaterialData() {
        return materialData;
    }

    @Override
    public void setMaterialData(MaterialData materialData) {
        this.materialData = materialData;
    }

    @Override
    public @Nullable MaterialMultiplier getMaterialMultiplier() {
        return materialMultiplier;
    }

    @Override
    public void setMaterialMultiplier(MaterialMultiplier materialMultiplier) {
        this.materialMultiplier = materialMultiplier;
    }
}
