package cn.origin.cube.guis.csgoGui.gui;

import cn.origin.cube.Cube;
import cn.origin.cube.guis.csgoGui.utils.Quad;
import cn.origin.cube.guis.csgoGui.utils.SkeetUtils;
import cn.origin.cube.module.AbstractModule;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SkeetGUI extends GuiScreen {
    public static int GUI_KEY = Keyboard.KEY_RSHIFT;

    public static int GUI_KEY_CLOSE = Keyboard.KEY_ESCAPE;

    public static String BACKGROUND = "BLUR";

    public static boolean PAUSE_GAME = false;

    public double posX, posY, width, height, dragX, dragY;
    public boolean dragging;
    public Category selectedCategory;

    private Module selectedModule;
    public int modeIndex;

    public ArrayList<Comp> comps = new ArrayList<>();

    public SkeetGUI() {
        MinecraftForge.EVENT_BUS.register(this);
        posX = 20;
        posY = 20;
        width = posX + 150 * 2;
        height = height + 200;
        selectedCategory = Category.COMBAT;
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Render2DUtil.drawGradientBorderedRect((float) posX,(float) posY,(float) width,(float) height, new Color(150,150,150,150).getRGB());

        int offset = 0;
        for (Category category : Category.values()) {
            Cube.fontManager.IconFont.drawString(category.getIcon(), (int) posX + 20 + offset, (int) (posY + 15),category.equals(selectedCategory) ? new Color(170, 255, 170).getRGB() : new Color(170, 170, 170).getRGB());
            Render2DUtil.drawGradientHLine((float)posX + 20, (float)posY + 20, (float)posX + 20 + offset,new Color(170, 255, 170).getRGB(), new Color(170, 170, 170).getRGB());
            offset += 25;
        }
        offset = 0;
        for (AbstractModule m : Cube.moduleManager.getModulesByCategory(selectedCategory)) {
            Cube.fontManager.CustomFont.drawString(m.name,(int)posX + 20, (int)(posY + 30) + offset,m.isEnabled() ? new Color(170, 255, 170).getRGB() : new Color(170,170,170).getRGB());
            offset += 10;
        }

        for (Comp comp : comps) {
            comp.drawScreen(mouseX, mouseY);
        }
        int divi=6;

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (Comp comp : comps) {
            comp.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isInside(mouseX, mouseY, posX, posY + 15, width, posY) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - posX;
            dragY = mouseY - posY;
        }
        int offset = 0;
        for (Category category : Category.values()) {
            if (isInside(mouseX, mouseY,posX + offset,posY + 1,posX + 20 + offset,posY + 20) && mouseButton == 0) {
                selectedCategory = category;
            }
            offset += 25;
        }
        offset = 0;
        for (AbstractModule m : Cube.moduleManager.getModulesByCategory(selectedCategory)) {
            if (isInside(mouseX, mouseY,posX + 20,posY + 30 + offset,posX + 90,posY + 40 + offset)) {
                if (mouseButton == 0) {
                    m.toggle();
                }
                if (mouseButton == 1) {
                    int sOffset = 3;
                    comps.clear();
//                    if ((m) != null)
//                        for (Setting setting : ExampleMod.instance.settingsManager.getSettingsByMod(m)) {
//                            selectedModule = m;
//                            if (setting.isCombo()) {
//                                comps.add(new Combo(275, sOffset, this, selectedModule, setting));
//                                sOffset += 15;
//                            }
//                            if (setting.isCheck()) {
//                                comps.add(new CheckBox(275, sOffset, this, selectedModule, setting));
//                                sOffset += 15;
//                            }
//                            if (setting.isSlider()) {
//                                comps.add(new Slider(275, sOffset, this, selectedModule, setting));
//                                sOffset += 25;
//                            }
//                            comps.add(new KeyBinds(275, sOffset, this, selectedModule, setting));
//                        }
                }
            }
            offset += 10;
        }
        for (Comp comp : comps) {
            comp.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
        for (Comp comp : comps) {
            comp.mouseReleased(mouseX, mouseY, state);
        }
    }

    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    @Override
    public void onGuiClosed() {
        switch (BACKGROUND) {
            case "BOTH":
            case "BLUR": {
                mc.entityRenderer.stopUseShader();
            }
        }
    }

    public void onGuiOpened() {
        switch (BACKGROUND) {
            case "BOTH":
            case "BLUR": {
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return PAUSE_GAME;
    }
}
