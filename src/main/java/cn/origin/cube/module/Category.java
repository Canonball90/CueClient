package cn.origin.cube.module;

import cn.origin.cube.utils.IconFontKt;

import java.awt.*;

public enum Category {
    COMBAT("Combat", IconFontKt.TARGET, false, new Color(255,0,0)),
    MOVEMENT("Movement", IconFontKt.METER, false, new Color(0, 176, 0)),
    VISUAL("Visual", IconFontKt.EYE, false, new Color(0, 0, 189)),
    WORLD("World", IconFontKt.EARTH, false, new Color(176, 176, 0)),
    FUNCTION("Function", IconFontKt.COGS, false, new Color(0, 189, 255)),
    CLIENT("Client", IconFontKt.EQUALIZER, false, new Color(182, 55, 0)),
    HUD("Hud", IconFontKt.PENCLI, true, new Color(161, 0, 197));

    private final String name;
    private final String icon;
    private final Color color;
    public final boolean isHud;

    Category(String name, String icon, boolean isHud, Color color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.isHud = isHud;

    }

    public String getName() {
        return name;
    }

    public Color getColor(){return color;}

    public String getIcon() {
        return icon;
    }
}
