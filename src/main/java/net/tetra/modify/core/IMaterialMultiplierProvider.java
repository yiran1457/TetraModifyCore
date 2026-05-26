package net.tetra.modify.core;

import org.jetbrains.annotations.Nullable;
import se.mickelus.tetra.module.data.MaterialMultiplier;

public interface IMaterialMultiplierProvider {
    @Nullable
    MaterialMultiplier getMaterialMultiplier();

    void setMaterialMultiplier(MaterialMultiplier materialMultiplier);
}
