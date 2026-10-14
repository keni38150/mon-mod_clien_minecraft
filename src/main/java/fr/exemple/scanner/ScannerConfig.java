package fr.exemple.scanner;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import java.nio.file.*;
public final class ScannerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("scanner.json");
    public static ModuleSettings storage = defaultStorage();
    public static ModuleSettings spawners = defaultSpawners();

    private static ModuleSettings defaultStorage() {
        ModuleSettings s = new ModuleSettings();
        s.targets.put("chest", new ModuleSettings.Target(0xFFA500, true));
        s.targets.put("trapped_chest", new ModuleSettings.Target(0xFF3B30, true));
        s.targets.put("ender_chest", new ModuleSettings.Target(0xB44CFF, true));
        s.targets.put("barrel", new ModuleSettings.Target(0x8B5A2B, true));
        s.targets.put("shulker", new ModuleSettings.Target(0xFF66CC, true));
        s.targets.put("dispenser", new ModuleSettings.Target(0x00C8FF, true));
        s.targets.put("dropper", new ModuleSettings.Target(0x3DDC84, true));
        s.targets.put("hopper", new ModuleSettings.Target(0xAAAAAA, false));
        return s;
    }
    private static ModuleSettings defaultSpawners() {
        ModuleSettings s = new ModuleSettings();
        s.mode = RenderMode.BOTH; s.maxDistance = 96;
        s.targets.put("spawner", new ModuleSettings.Target(0xFFE600, true));
        s.targets.put("trial_spawner", new ModuleSettings.Target(0x00FFD5, true));
        return s;
    }
    public static void load() {
        try {
            if (!Files.exists(FILE)) { save(); return; }
            JsonObject o = GSON.fromJson(Files.readString(FILE), JsonObject.class);
            if (o.has("storage")) storage = merge(GSON.fromJson(o.get("storage"), ModuleSettings.class), defaultStorage());
            if (o.has("spawners")) spawners = merge(GSON.fromJson(o.get("spawners"), ModuleSettings.class), defaultSpawners());
        } catch (Exception e) { e.printStackTrace(); }
    }
    private static ModuleSettings merge(ModuleSettings loaded, ModuleSettings defaults) {
        defaults.targets.forEach(loaded.targets::putIfAbsent); return loaded;
    }
    public static void save() {
        try { JsonObject o = new JsonObject(); o.add("storage", GSON.toJsonTree(storage)); o.add("spawners", GSON.toJsonTree(spawners));
            Files.writeString(FILE, GSON.toJson(o)); } catch (Exception e) { e.printStackTrace(); }
    }
}
