package fr.exemple.scanner;
public enum RenderMode { OUTLINE("Contour"), FILLED("Rempli"), BOTH("Contour + rempli");
    public final String label; RenderMode(String l) { label = l; }
    public RenderMode next() { return values()[(ordinal() + 1) % values().length]; } }
