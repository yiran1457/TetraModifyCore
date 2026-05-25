package net.tetra.modify.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class MixinConfig {
    public static int nowVersion = 1;
    public static Gson GSON = new GsonBuilder().create();
    public static Path configPath = FMLPaths.CONFIGDIR.get().resolve("TetraModifyCoreMixinConfig.json");
    private static MixinConfig instance;
    public int version = nowVersion;
    public String thread_desc = "设定statBar多线程更新的线程数,设置为0关闭多线程优化";
    public int thread = 0;

    public static MixinConfig getInstance() {
        if (instance == null) {
            instance = buildInstance();
        }
        return instance;
    }

    private static MixinConfig buildInstance() {
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                MixinConfig config = GSON.fromJson(reader, MixinConfig.class);
                if (config.version != nowVersion) {
                    config.version = nowVersion;
                    writeConfig(config);
                }
                return config;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            MixinConfig config = new MixinConfig();
            writeConfig(config);
            return config;
        }
    }

    private static void writeConfig(MixinConfig config) {
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("\t");
            jsonWriter.setSerializeNulls(true);
            jsonWriter.setLenient(true);
            Streams.write(GSON.toJsonTree(config, MixinConfig.class), jsonWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
