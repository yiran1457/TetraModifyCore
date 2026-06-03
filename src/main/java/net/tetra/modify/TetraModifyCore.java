package net.tetra.modify;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tetra.modify.data.ScrollDataManager;
import org.slf4j.Logger;
import se.mickelus.tetra.blocks.scroll.ScrollItem;

@Mod(TetraModifyCore.MODID)
@SuppressWarnings("removal")
public class TetraModifyCore {
    public static final String MODID = "tetra_modify_core";
    private static final Logger LOGGER = LogUtils.getLogger();
    DeferredRegister<CreativeModeTab> creativeTabs = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    RegistryObject<CreativeModeTab> creativeTab = creativeTabs.register("scroll", () ->
            CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ScrollItem.instance))
                    .title(Component.translatable("tetra_modify_core.scrollGroup"))
                    .displayItems((itemDisplayParameters, output) -> {
                        ScrollDataManager.addScrollItems(output);
                        ScrollItem.instance.getCreativeTabItems().forEach(output::accept);
                    }).build());

    public TetraModifyCore() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus gameBus = MinecraftForge.EVENT_BUS;
        modBus.register(ScrollDataManager.class);
        creativeTabs.register(modBus);
    }
}
