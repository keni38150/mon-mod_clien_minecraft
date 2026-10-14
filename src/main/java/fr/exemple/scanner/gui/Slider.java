package fr.exemple.scanner.gui;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;
public class Slider extends AbstractSliderButton {
    private final String label; private final double min, max; private final boolean integer; private final Consumer<Double> onChange;
    public Slider(int x, int y, int w, String label, double min, double max, double val, boolean integer, Consumer<Double> onChange) {
        super(x, y, w, 20, Component.empty(), (val - min) / (max - min));
        this.label = label; this.min = min; this.max = max; this.integer = integer; this.onChange = onChange; updateMessage();
    }
    private double real() { return min + value * (max - min); }
    @Override protected void updateMessage() {
        if (label == null) return;
        double v = real();
        setMessage(Component.literal(label + " : " + (integer ? String.valueOf((int) Math.round(v)) : String.format("%.2f", v))));
    }
    @Override protected void applyValue() { double v = real(); onChange.accept(integer ? Math.round(v) : v); }
}
