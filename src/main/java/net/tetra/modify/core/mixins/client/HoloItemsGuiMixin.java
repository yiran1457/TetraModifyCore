package net.tetra.modify.core.mixins.client;

import net.minecraft.world.item.ItemStack;
import net.tetra.modify.client.TemporaryHoloItemGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.HoloGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloCraftRootGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloItemsGui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(value = HoloItemsGui.class, remap = false)
public class HoloItemsGuiMixin extends GuiElement {
    BiConsumer<IModularItem, ItemStack> onItemSelect;
    Consumer<String> onSlotSelect;

    public HoloItemsGuiMixin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void z(int x, int y, int width, int height, BiConsumer<IModularItem, ItemStack> onItemSelect, Consumer<String> onSlotSelect, Runnable onMaterialsClick, CallbackInfo ci) {
        this.onItemSelect = onItemSelect;
        this.onSlotSelect = onSlotSelect;
    }

    @Unique
    ItemStack getCurrentStack() {
        return ((HoloCraftRootGui) HoloGui.getInstance().pages[0]).itemStack;
    }

    //清除缓存
    @Inject(method = "changeItem", at = @At("HEAD"))
    private void clearTemporary(IModularItem item, CallbackInfo ci) {
        elements.removeIf(guiElement -> guiElement instanceof TemporaryHoloItemGui);
    }

    @Inject(method = "changeItem", at = @At(value = "INVOKE", target = "Lse/mickelus/tetra/items/modular/impl/holo/gui/craft/HoloMaterialsButtonGui;setVisible(Z)V"))
    private void addTemporary(IModularItem item, CallbackInfo ci) {
        if (getCurrentStack() != null)
            //如果没有可展示物品，则进入缓存物品展示
            if (this.getChildren(HoloItemGui.class).stream().noneMatch(holoItemGui -> holoItemGui.isSelected)) {
                var temporary = new TemporaryHoloItemGui(item, getCurrentStack(), () -> onItemSelect.accept(item, getCurrentStack()), onSlotSelect);
                temporary.setAttachment(GuiAttachment.topCenter);
                addChild(temporary);
                temporary.onItemSelected(item);
            }
    }
}
