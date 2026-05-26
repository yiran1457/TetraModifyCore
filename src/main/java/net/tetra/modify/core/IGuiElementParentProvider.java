package net.tetra.modify.core;

import org.jetbrains.annotations.Nullable;
import se.mickelus.mutil.gui.GuiElement;

public interface IGuiElementParentProvider {
    @Nullable
    GuiElement getParent();

    void setParent(GuiElement guiElement);
}
