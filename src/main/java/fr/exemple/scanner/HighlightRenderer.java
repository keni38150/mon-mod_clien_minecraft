package fr.exemple.scanner;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.List;

/** Rendu via les « Gizmos » de Minecraft (API de debug) : pas de pipeline de rendu à écrire. */
public final class HighlightRenderer {
    public static void init() { LevelRenderEvents.DEBUG_RENDER.register(ctx -> frame()); }

    private static void frame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        Vec3 eye = mc.player.getEyePosition();
        draw(eye, Detector.storageHits, ScannerConfig.storage);
        draw(eye, Detector.spawnerHits, ScannerConfig.spawners);
    }

    private static void draw(Vec3 eye, List<Detector.Hit> hits, ModuleSettings s) {
        if (!s.enabled || hits.isEmpty()) return;
        double max2 = (double) s.maxDistance * s.maxDistance;
        boolean outline = s.mode != RenderMode.FILLED, fill = s.mode != RenderMode.OUTLINE;
        for (Detector.Hit h : hits) {
            ModuleSettings.Target t = s.targets.get(h.type());
            if (t == null || !t.enabled) continue;
            double d2 = h.box().getCenter().distanceToSqr(eye);
            if (d2 > max2) continue;
            float fade = s.fadeWithDistance ? (float) Math.max(0.1, 1.0 - Math.sqrt(d2 / max2)) : 1f;
            int rgb = t.rgb & 0xFFFFFF;
            int strokeArgb = (alpha(s.outlineOpacity * fade) << 24) | rgb;
            int fillArgb = (alpha(s.fillOpacity * fade) << 24) | rgb;
            AABB bx = h.box().inflate(0.002);
            GizmoStyle style = outline && fill ? GizmoStyle.strokeAndFill(strokeArgb, s.lineWidth, fillArgb)
                             : outline ? GizmoStyle.stroke(strokeArgb, s.lineWidth) : GizmoStyle.fill(fillArgb);
            var props = Gizmos.cuboid(bx, style);
            if (s.throughWalls) props.setAlwaysOnTop();
        }
    }
    private static int alpha(float v) { return Math.round(Math.max(0f, Math.min(1f, v)) * 255); }
}
