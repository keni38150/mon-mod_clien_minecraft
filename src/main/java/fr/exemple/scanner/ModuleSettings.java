package fr.exemple.scanner;
import java.util.LinkedHashMap;
import java.util.Map;
/** Réglages d'un module (stockage ou générateurs). Sérialisé en JSON. */
public class ModuleSettings {
    public static class Target { public boolean enabled = true; public int rgb; public Target() {} public Target(int rgb, boolean en) { this.rgb = rgb; this.enabled = en; } }
    public boolean enabled = true;
    public RenderMode mode = RenderMode.OUTLINE;
    public float outlineOpacity = 1.0f;
    public float fillOpacity = 0.25f;
    public float lineWidth = 2.0f;
    public int maxDistance = 64;
    public boolean throughWalls = true;
    public boolean fadeWithDistance = false;
    public Map<String, Target> targets = new LinkedHashMap<>();
}
