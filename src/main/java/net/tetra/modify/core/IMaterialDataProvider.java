package net.tetra.modify.core;

import org.jetbrains.annotations.Nullable;
import se.mickelus.tetra.module.data.MaterialData;

public interface IMaterialDataProvider {
    @Nullable
    MaterialData getMaterialData();

    void setMaterialData(MaterialData materialData);
}
