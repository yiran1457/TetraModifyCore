package net.tetra.modify.client;

import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemGui;

import java.util.function.Consumer;

public class TemporaryHoloItemGui extends HoloItemGui {

    public TemporaryHoloItemGui(IModularItem item, ItemStack stack, Runnable onSelect, Consumer<String> onSlotSelect) {
        super(0, 0, item, stack, 0, onSelect, onSlotSelect);
        elements.removeIf(this.icon::equals);
        var name = new GuiStringOutline(0, -30, stack.getHoverName().getString());
        name.setAttachment(GuiAttachment.middleCenter);
        this.addChild(name);
        GuiElement z;
        z = new GuiTexture(0, 0, 16, 16, 256 - 14, 0, GuiTextures.toolActions);
        z.setAttachment(GuiAttachment.middleCenter);
        this.addChild(z);
    }
}
