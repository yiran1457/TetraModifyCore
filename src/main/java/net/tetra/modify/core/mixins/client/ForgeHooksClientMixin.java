package net.tetra.modify.core.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ForgeHooksClient.class, remap = false)
public class ForgeHooksClientMixin {
    @Inject(method = "gatherTooltipComponentsFromElements", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/event/RenderTooltipEvent$GatherComponents;getMaxWidth()I", ordinal = 0))
    private static void tmc$alwaysNeedsWrap(ItemStack stack, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont, CallbackInfoReturnable<List<ClientTooltipComponent>> cir, @Local(name = "needsWrap") LocalBooleanRef needsWrap) {
        needsWrap.set(true);
    }
}
