package net.tetra.modify.core;

import org.jetbrains.annotations.Nullable;
import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.data.MaterialMultiplier;

public interface IMaterialInfoProvider {
    boolean hasInfo();

    void markHasInfo();

    @Nullable
    MaterialMultiplier getMaterialMultiplier();

    void setMaterialMultiplier(MaterialMultiplier materialMultiplier);

    @Nullable
    MaterialData getMaterialData();

    void setMaterialData(MaterialData materialData);
}
