package dev.canon.cue.font;

import dev.canon.cue.Cue;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class FontManager {
    public MinecraftFontRenderer CustomFont;

    public MinecraftFontRenderer IconFont;

    public MinecraftFontRenderer badaboom;

    public FontManager() throws IOException, FontFormatException {
        CustomFont = new MinecraftFontRenderer(Font.createFont(Font.PLAIN, Objects.requireNonNull(Cue.class.getResourceAsStream("/assets/fonts/CustomFont.ttf"))).deriveFont(38f));
        IconFont = new MinecraftFontRenderer(Font.createFont(Font.PLAIN, Objects.requireNonNull(Cue.class.getResourceAsStream("/assets/fonts/CubeBaseIcon.ttf"))).deriveFont(38f));
        badaboom = new MinecraftFontRenderer(Font.createFont(Font.PLAIN, Objects.requireNonNull(Cue.class.getResourceAsStream("/assets/fonts/badaboom.ttf"))).deriveFont(38f));
    }

    public MinecraftFontRenderer getCustomFont(float size) {
        try {
            return new MinecraftFontRenderer(Font.createFont(Font.PLAIN, Objects.requireNonNull(Cue.class.getResourceAsStream("/assets/fonts/CustomFont.ttf"))).deriveFont(size));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MinecraftFontRenderer getBadaboom(float size) {
        try {
            return new MinecraftFontRenderer(Font.createFont(Font.PLAIN, Objects.requireNonNull(Cue.class.getResourceAsStream("/assets/fonts/badaboom.ttf"))).deriveFont(size));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MinecraftFontRenderer getIconFont(float size) {
        try {
            return new MinecraftFontRenderer(Font.createFont(Font.PLAIN, Objects.requireNonNull(Cue.class.getResourceAsStream("/assets/fonts/CubeBaseIcon.ttf"))).deriveFont(size));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
