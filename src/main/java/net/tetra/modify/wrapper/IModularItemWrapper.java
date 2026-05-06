package net.tetra.modify.wrapper;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.items.modular.IModularItem;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IModularItemWrapper {
    @SuppressWarnings("unchecked")
    public static <T extends Item & IModularItem> T cast(Item item) {
        return (T) item;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client{

        public static List<Component> getTooltip(ItemStack itemStack, @Nullable Level world, TooltipFlag advanced){
            List<Component> tooltip = Lists.newArrayList();
            var item = IModularItemWrapper.cast(itemStack.getItem());
            if (item.isBroken(itemStack)) {
                tooltip.add(Component.translatable("item.tetra.modular.broken")
                        .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
            }

            if (Screen.hasShiftDown()) {
                tooltip.add(Tooltips.expanded);
                Arrays.stream(item.getMajorModules(itemStack))
                        .filter(Objects::nonNull)
                        .forEach(module -> {

                            tooltip.add(Component.literal("\u00BB ").withStyle(ChatFormatting.DARK_GRAY)
                                    .append(Component.literal(module.getName(itemStack)).withStyle(ChatFormatting.GRAY)));

                            module.getEnchantments(itemStack).entrySet().stream()
                                    .map(entry -> entry.getKey().getFullname(entry.getValue()))
                                    .map(text -> Component.literal("  - " + text.getString()))
                                    .map(text -> text.withStyle(ChatFormatting.DARK_GRAY))
                                    .forEach(tooltip::add);

                            Arrays.stream(module.getImprovements(itemStack))
                                    .map(improvement -> "  - " + item.getImprovementTooltip(improvement.key, improvement.level, true))
                                    .map(Component::literal)
                                    .map(textComponent -> textComponent.withStyle(ChatFormatting.DARK_GRAY))
                                    .forEach(tooltip::add);
                        });
                Arrays.stream(item.getMinorModules(itemStack))
                        .filter(Objects::nonNull)
                        .map(module -> Component.literal(" * ").withStyle(ChatFormatting.DARK_GRAY)
                                .append(Component.literal(module.getName(itemStack)).withStyle(ChatFormatting.GRAY)))
                        .forEach(tooltip::add);

                // honing tooltip
                if (ConfigHandler.moduleProgression.get() && item.canGainHoneProgress(itemStack)) {
                    if (IModularItem.isHoneable(itemStack)) {
                        tooltip.add(Component.literal(" > ").withStyle(ChatFormatting.AQUA)
                                .append(Component.translatable("tetra.hone.available").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY))));
                    } else {
                        int progress = item.getHoningProgress(itemStack);
                        int base = item.getHoningLimit(itemStack);
                        String percentage = String.format("%.0f", 100f * (base - progress) / base);
                        tooltip.add(Component.literal(" > ").withStyle(ChatFormatting.DARK_AQUA)
                                .append(Component.translatable("tetra.hone.progress", base - progress, base, percentage).withStyle(ChatFormatting.GRAY)));
                    }
                }
            } else {
                ItemStack.appendEnchantmentNames(tooltip, itemStack.getEnchantmentTags());

                tooltip.add(Tooltips.expand);
            }

            return tooltip;
        }
    }

}
