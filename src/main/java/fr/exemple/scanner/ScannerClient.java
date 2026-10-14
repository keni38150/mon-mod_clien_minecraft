package fr.exemple.scanner;
import fr.exemple.scanner.gui.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ScannerClient implements ClientModInitializer {
    private static KeyMapping openGui;
    @Override public void onInitializeClient() {
        ScannerConfig.load();
        KeyMapping.Category cat = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("scanner", "main"));
        openGui = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.scanner.gui", GLFW.GLFW_KEY_RIGHT_SHIFT, cat));
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (openGui.consumeClick()) mc.gui.setScreen(new ConfigScreen(mc.gui.screen()));
            Detector.tick(mc);
        });
        HighlightRenderer.init();
    }
}
