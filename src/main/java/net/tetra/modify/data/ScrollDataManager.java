package net.tetra.modify.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.util.HexCodec;
import se.mickelus.tetra.blocks.scroll.ScrollData;
import se.mickelus.tetra.blocks.scroll.ScrollItem;
import se.mickelus.tetra.data.DataManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ScrollDataManager extends SimpleJsonResourceReloadListener {
    public static ScrollDataManager instance = new ScrollDataManager();
    public static List<ScrollData[]> scrollData = new ObjectArrayList<>();
    public static Codec<Integer> IntClamp = Codec.INT.xmap(
            i -> Mth.clamp(i, 0, 15),
            i -> Mth.clamp(i, 0, 15)
    );
    public static Codec<ScrollData> ScrollDataCodec = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("key").forGetter(i -> i.key),
            Codec.STRING.optionalFieldOf("details").forGetter(i -> Optional.ofNullable(i.details)),
            Codec.BOOL.fieldOf("intricate").forGetter(i -> i.isIntricate),
            Codec.INT.fieldOf("material").forGetter(i -> i.material),
            HexCodec.instance.fieldOf("ribbon").forGetter(i -> i.ribbon),
            IntClamp.listOf().optionalFieldOf("glyphs", Collections.emptyList()).forGetter(i -> i.glyphs),
            ResourceLocation.CODEC.listOf().optionalFieldOf("schematics", Collections.emptyList()).forGetter(i -> i.schematics),
            ResourceLocation.CODEC.listOf().optionalFieldOf("effects", Collections.emptyList()).forGetter(i -> i.craftingEffects)
    ).apply(instance, ScrollData::new));

    public ScrollDataManager() {
        super(DataManager.gson, "scrolls");
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(instance);
    }

    public static void addScrollItems(CreativeModeTab.Output output) {
        scrollData.stream()
                .flatMap(Stream::of)
                .map(ScrollDataManager::getScrollItem)
                .forEach(output::accept);
    }

    public static ItemStack getScrollItem(ScrollData scrollData) {
        var stack = new ItemStack(ScrollItem.instance);
        var tag = new CompoundTag();
        stack.addTagElement("BlockEntityTag", tag);
        var list = new ListTag();
        tag.put("data", list);
        list.add(ScrollDataCodec.encodeStart(NbtOps.INSTANCE, scrollData).result().get());
        return stack;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        scrollData.clear();
        for (JsonElement value : jsonElementMap.values()) {
            scrollData.add(DataManager.gson.fromJson(value, ScrollData[].class));
        }
    }
}
