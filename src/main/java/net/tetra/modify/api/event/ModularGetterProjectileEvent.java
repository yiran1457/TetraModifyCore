package net.tetra.modify.api.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import se.mickelus.tetra.items.modular.IModularItem;

import java.util.function.Predicate;

@Cancelable
public class ModularGetterProjectileEvent extends Event {
    public IModularItem modularItem;
    public ItemStack modularStack;
    public Player shooter;
    private int heldPriority = 0;
    private int allPriority = 0;
    private boolean isHandle = false;
    private Predicate<ItemStack> heldPredicate = null;
    private Predicate<ItemStack> allPredicate = null;

    public ModularGetterProjectileEvent(IModularItem modularItem, ItemStack stack, Player player) {
        this.modularItem = modularItem;
        this.modularStack = stack;
        this.shooter = player;
    }

    public static ItemStack onGetter(IModularItem iModularItem, Player instance, ItemStack shootable, ItemStack bowStack) {
        var event = new ModularGetterProjectileEvent(iModularItem, bowStack, instance);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return ItemStack.EMPTY;
        }
        if (!event.isHandle()) {
            return instance.getProjectile(shootable);
        } else {
            //获取主手物品
            Predicate<ItemStack> predicate = event.getSupportedHeldProjectiles();
            ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(instance, predicate);
            if (!itemstack.isEmpty()) {
                //先用着，不确定会不会有mod强转导致崩溃
                return ForgeHooks.getProjectile(instance, bowStack, itemstack);
            } else {
                predicate = event.getAllSupportedProjectiles();

                for (int i = 0; i < instance.getInventory().getContainerSize(); ++i) {
                    ItemStack itemstack1 = instance.getInventory().getItem(i);
                    if (predicate.test(itemstack1)) {
                        return ForgeHooks.getProjectile(instance, bowStack, itemstack1);
                    }
                }

                return ForgeHooks.getProjectile(instance, bowStack, instance.getAbilities().instabuild ? new ItemStack(Items.ARROW) : ItemStack.EMPTY);
            }
        }
    }

    public boolean setHeldPredicate(int heldPriority, Predicate<ItemStack> heldPredicate) {
        if (heldPriority > this.heldPriority) {
            this.heldPredicate = heldPredicate;
            this.heldPriority = heldPriority;
            isHandle = true;
            return true;
        }
        return false;
    }

    public boolean setAllPredicate(int allPriority, Predicate<ItemStack> allPredicate) {
        if (allPriority > this.allPriority) {
            this.allPredicate = allPredicate;
            this.allPriority = allPriority;
            isHandle = true;
            return true;
        }
        return false;
    }

    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return heldPredicate != null ? heldPredicate : allPredicate;
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return allPredicate != null ? allPredicate : heldPredicate;
    }

    public boolean isHandle() {
        return isHandle;
    }

    public IModularItem getModularItem() {
        return modularItem;
    }

    public ItemStack getModularStack() {
        return modularStack;
    }

    public Player getShooter() {
        return shooter;
    }
}
