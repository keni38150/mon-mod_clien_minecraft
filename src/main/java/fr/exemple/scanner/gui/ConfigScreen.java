package fr.exemple.scanner.gui;
import fr.exemple.scanner.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {
    private final Screen parent; private boolean storageTab = true; private int targetIdx = 0;
    public ConfigScreen(Screen parent) { super(Component.literal("Scanner Client")); this.parent = parent; }
    private ModuleSettings cur() { return storageTab ? ScannerConfig.storage : ScannerConfig.spawners; }

    @Override protected void init() {
        int w = 220, x = width / 2 - w / 2, y = 24, gap = 24;
        ModuleSettings s = cur();
        addRenderableWidget(new StringWidget(x, 8, w, 12, Component.literal("§lScanner Client"), font));
        addRenderableWidget(Button.builder(Component.literal(storageTab ? "▶ Stockage" : "Stockage"), b -> { storageTab = true; targetIdx = 0; rebuildWidgets(); }).bounds(x, y, w / 2 - 2, 20).build());
        addRenderableWidget(Button.builder(Component.literal(!storageTab ? "▶ Générateurs" : "Générateurs"), b -> { storageTab = false; targetIdx = 0; rebuildWidgets(); }).bounds(x + w / 2 + 2, y, w / 2 - 2, 20).build()); y += gap + 4;

        addRenderableWidget(Button.builder(Component.literal("Module : " + (s.enabled ? "§aActivé" : "§cDésactivé")), b -> { s.enabled = !s.enabled; rebuildWidgets(); }).bounds(x, y, w, 20).build()); y += gap;
        addRenderableWidget(Button.builder(Component.literal("Mode : " + s.mode.label), b -> { s.mode = s.mode.next(); rebuildWidgets(); }).bounds(x, y, w, 20).build()); y += gap;
        addRenderableWidget(new Slider(x, y, w, "Opacité contour", 0, 1, s.outlineOpacity, false, v -> s.outlineOpacity = v.floatValue())); y += gap;
        addRenderableWidget(new Slider(x, y, w, "Opacité remplissage", 0, 1, s.fillOpacity, false, v -> s.fillOpacity = v.floatValue())); y += gap;
        addRenderableWidget(new Slider(x, y, w, "Épaisseur", 1, 6, s.lineWidth, false, v -> s.lineWidth = v.floatValue())); y += gap;
        addRenderableWidget(new Slider(x, y, w, "Distance max", 8, 256, s.maxDistance, true, v -> s.maxDistance = v.intValue())); y += gap;
        addRenderableWidget(Button.builder(Component.literal("À travers les murs : " + (s.throughWalls ? "Oui" : "Non")), b -> { s.throughWalls = !s.throughWalls; rebuildWidgets(); }).bounds(x, y, w / 2 - 2, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Fondu : " + (s.fadeWithDistance ? "Oui" : "Non")), b -> { s.fadeWithDistance = !s.fadeWithDistance; rebuildWidgets(); }).bounds(x + w / 2 + 2, y, w / 2 - 2, 20).build()); y += gap + 4;

        List<String> keys = new ArrayList<>(s.targets.keySet());
        if (targetIdx >= keys.size()) targetIdx = 0;
        String key = keys.get(targetIdx); ModuleSettings.Target t = s.targets.get(key);
        addRenderableWidget(Button.builder(Component.literal("Bloc : " + key + " ⟳"), b -> { targetIdx = (targetIdx + 1) % keys.size(); rebuildWidgets(); }).bounds(x, y, w * 2 / 3 - 2, 20).build());
        addRenderableWidget(Button.builder(Component.literal(t.enabled ? "§aON" : "§cOFF"), b -> { t.enabled = !t.enabled; rebuildWidgets(); }).bounds(x + w * 2 / 3 + 2, y, w / 3 - 2, 20).build()); y += gap;
        int sw = (w - 8) / 3;
        addRenderableWidget(new Slider(x, y, sw, "R", 0, 255, (t.rgb >> 16) & 255, true, v -> t.rgb = (t.rgb & 0x00FFFF) | (v.intValue() << 16)));
        addRenderableWidget(new Slider(x + sw + 4, y, sw, "V", 0, 255, (t.rgb >> 8) & 255, true, v -> t.rgb = (t.rgb & 0xFF00FF) | (v.intValue() << 8)));
        addRenderableWidget(new Slider(x + 2 * (sw + 4), y, sw, "B", 0, 255, t.rgb & 255, true, v -> t.rgb = (t.rgb & 0xFFFF00) | v.intValue())); y += gap + 4;
        addRenderableWidget(Button.builder(Component.literal("Terminé"), b -> onClose()).bounds(x, y, w, 20).build());
    }
    @Override public void onClose() { ScannerConfig.save(); minecraft.gui.setScreen(parent); }
}
