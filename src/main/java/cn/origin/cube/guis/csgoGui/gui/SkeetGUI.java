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
        SkeetUtils.renderSkeetBox(new Quad(10, 10, 400, 400));

        int offset = 0;
        for (Category category : Category.values()) {
            Render2DUtil.drawRect1((int) (posX), (int) (posY + 1 + offset), (int) (posX + 50), (int) (posY + 35 + offset),category.equals(selectedCategory) ? new Color(55, 255, 0).getRGB() : new Color(28, 28, 28).getRGB());
            Cube.fontManager.IconFont.drawString(category.getIcon(), (int) posX + 20, (int) (posY + 15) + offset, new Color(170, 170, 170).getRGB());
            offset += 50;
        }
        Render2DUtil.drawRect1((int) (posX), (int) (posY + 1 + offset), (int) (posX + 50), (int) (posY + 390 + offset),new Color(10, 10, 10).getRGB());

        offset = 0;
        for (AbstractModule m : Cube.moduleManager.getModulesByCategory(selectedCategory)) {
            Render2DUtil.drawRect1((int) (posX + 65), (int) (posY + 1 + offset), (int) (posX + 135), (int) (posY + 15 + offset),m.isEnabled() ? new Color(55,255,0).getRGB() : new Color(28,28,28).getRGB());
            Minecraft.getMinecraft().fontRenderer.drawString(m.name,(int)posX + 67, (int)(posY + 5) + offset, new Color(170,170,170).getRGB());
            offset += 15;
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
        if (isInside(mouseX, mouseY, posX, posY - 10, width, posY) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - posX;
            dragY = mouseY - posY;
        }
        int offset = 0;
        for (Category category : Category.values()) {
            if (isInside(mouseX, mouseY,posX,posY + 1 + offset,posX + 60,posY + 45 + offset) && mouseButton == 0) {
                selectedCategory = category;
            }
            offset += 50;
        }
        offset = 0;
        for (AbstractModule m : Cube.moduleManager.getModulesByCategory(selectedCategory)) {
            if (isInside(mouseX, mouseY,posX + 65,posY + 1 + offset,posX + 125,posY + 15 + offset)) {
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
            offset += 15;
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
