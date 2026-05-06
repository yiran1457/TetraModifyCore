package net.tetra.modify.utils;

import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.IModularItem;

public class MatchUtils {
    public static boolean matchItem(ItemStack stack, ItemStack other) {
        if (!(stack.getItem() instanceof IModularItem)) return false;
        if (!(other.getItem() instanceof IModularItem)) return false;
        if (stack == other) return true;
        if (stack.getItem() != other.getItem()) return false;
        if (stack.hasTag() && other.hasTag()) {
            return stack.getTag().getString("id").equals(other.getTag().getString("id"));
        }
        return true;
    }
}
