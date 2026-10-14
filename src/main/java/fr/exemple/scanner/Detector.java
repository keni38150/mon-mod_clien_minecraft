package fr.exemple.scanner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.ArrayList;
import java.util.List;

/** Parcourt les block entities des chunks chargés (léger : toutes les 10 ticks). */
public final class Detector {
    public record Hit(BlockPos pos, String type, AABB box) {}
    public static volatile List<Hit> storageHits = List.of();
    public static volatile List<Hit> spawnerHits = List.of();
    private static int tick;

    public static void tick(Minecraft mc) {
        if (mc.level == null || mc.player == null) { storageHits = List.of(); spawnerHits = List.of(); return; }
        if (++tick % 10 != 0) return;
        ClientLevel level = mc.level;
        boolean wantS = ScannerConfig.storage.enabled, wantG = ScannerConfig.spawners.enabled;
        List<Hit> s = new ArrayList<>(), g = new ArrayList<>();
        if (wantS || wantG) {
            ChunkPos pc = mc.player.chunkPosition();
            int r = Math.max(2, mc.options.getEffectiveRenderDistance());
            for (int dx = -r; dx <= r; dx++) for (int dz = -r; dz <= r; dz++) {
                ChunkAccess ca = level.getChunk(pc.x() + dx, pc.z() + dz, ChunkStatus.FULL, false);
                if (!(ca instanceof LevelChunk chunk)) continue;
                for (BlockEntity be : chunk.getBlockEntities().values()) {
                    String t = storageType(be);
                    if (t != null && wantS) { s.add(new Hit(be.getBlockPos(), t, box(level, be))); continue; }
                    t = spawnerType(be);
                    if (t != null && wantG) g.add(new Hit(be.getBlockPos(), t, box(level, be)));
                }
            }
        }
        storageHits = s; spawnerHits = g;
    }
    private static AABB box(ClientLevel level, BlockEntity be) {
        BlockPos p = be.getBlockPos();
        VoxelShape sh = be.getBlockState().getShape(level, p);
        return (sh.isEmpty() ? new AABB(0, 0, 0, 1, 1, 1) : sh.bounds()).move(p);
    }
    static String storageType(BlockEntity be) {
        if (be instanceof TrappedChestBlockEntity) return "trapped_chest";
        if (be instanceof ChestBlockEntity) return "chest";
        if (be instanceof EnderChestBlockEntity) return "ender_chest";
        if (be instanceof BarrelBlockEntity) return "barrel";
        if (be instanceof ShulkerBoxBlockEntity) return "shulker";
        if (be instanceof DropperBlockEntity) return "dropper";
        if (be instanceof DispenserBlockEntity) return "dispenser";
        if (be instanceof HopperBlockEntity) return "hopper";
        return null;
    }
    static String spawnerType(BlockEntity be) {
        if (be instanceof SpawnerBlockEntity) return "spawner";
        if (be instanceof TrialSpawnerBlockEntity) return "trial_spawner";
        return null;
    }
}
