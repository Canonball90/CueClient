package cn.origin.cube.guis.csgoGui.gui;


import cn.origin.cube.core.module.AbstractModule;
import cn.origin.cube.core.settings.Setting;

public class Comp {

    public double x, y, x2, y2;
    public SkeetGUI parent;
    public AbstractModule module;
    public Setting setting;

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public void drawScreen(int mouseX, int mouseY) {

    }

    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    public void keyTyped(char typedChar, int keyCode) {

    }

}