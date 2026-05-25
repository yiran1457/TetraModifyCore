package net.tetra.modify.core.mixins;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tetra.modify.core.MixinConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.bar.GuiStatBase;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mixin(value = GuiStatBar.class, remap = false)
public abstract class GuiStatBarMixin extends GuiStatBase {
    private static ExecutorService executor = Executors.newFixedThreadPool(MixinConfig.getInstance().thread);

    @Shadow
    protected String labelKey;

    @Shadow
    protected GuiString labelString;

    @Shadow
    protected IStatGetter statGetter;
    @Shadow
    protected List<Component> tooltip;
    @Shadow
    protected List<Component> extendedTooltip;

    public GuiStatBarMixin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Shadow
    protected abstract List<Component> getCombinedTooltip(Player player, ItemStack itemStack);

    @Shadow
    protected abstract List<Component> getCombinedTooltipExtended(Player player, ItemStack itemStack);

    @Shadow
    protected abstract double getSlotValue(Player player, ItemStack itemStack, @Nullable String slot, @Nullable String improvement);

    @Shadow
    public abstract void updateValue(double value, double diffValue);

    @Shadow
    protected abstract void updateIndicators(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement);

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void uuu(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement, CallbackInfo ci) {
        if (this.labelKey != null) {
            this.labelString.setString(I18n.get(this.labelKey, new Object[0]));
        }
        this.labelString.setVisible(this.labelKey != null);
        double value;
        double diffValue;
        if (!previewStack.isEmpty()) {
            value = this.statGetter.getValue(player, currentStack);
            diffValue = this.statGetter.getValue(player, previewStack);
            executor.execute(() -> {
                this.tooltip = this.getCombinedTooltip(player, previewStack);
                this.extendedTooltip = this.getCombinedTooltipExtended(player, previewStack);
            });
        } else {
            value = this.statGetter.getValue(player, currentStack);
            if (slot != null) {
                diffValue = value;
                value -= this.getSlotValue(player, currentStack, slot, improvement);
            } else {
                diffValue = value;
            }

            executor.execute(() -> {
                this.tooltip = this.getCombinedTooltip(player, currentStack);
                this.extendedTooltip = this.getCombinedTooltipExtended(player, currentStack);
            });
        }

        this.updateValue(value, diffValue);
        executor.execute(() -> {
            this.updateIndicators(player, currentStack, previewStack, slot, improvement);
        });
        ci.cancel();
    }
}
