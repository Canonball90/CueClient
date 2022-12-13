//package cn.origin.cube.guis.csgoGui.gui.comps;
//
//import com.example.examplemod.Module.Module;
//import com.example.examplemod.Utils.RenderUtils;
//import font.FontUtils;
//import cn.origin.cube.guis.csgoGui.gui.Comp;
//import cn.origin.cube.guis.csgoGui.gui.SkeetGUI;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Gui;
//import org.lwjgl.input.Keyboard;
//import yea.bushroot.clickgui.Setting;
//
//import java.awt.*;
//
//public class KeyBinds extends Comp {
//
//    private boolean binding;
//
//    public KeyBinds(double x, double y, SkeetGUI parent, Module module, Setting setting) {
//        this.x = x;
//        this.y = y;
//        this.parent = parent;
//        this.module = module;
//        this.setting = setting;
//    }
//
//    @Override
//    public void drawScreen(int mouseX, int mouseY) {
//
//        FontUtils.normal.drawString(setting.getName() + ": " + setting.getValString(),(int)(parent.posX + x - 70),(int)(parent.posY + y + 5), new Color(200,200,200).getRGB());
//        super.drawScreen(mouseX, mouseY);
//    }
//
//    @Override
//    public void mouseClicked(int mouseX, int mouseY, int button) {
//        if(isMouseOnButton(mouseX, mouseY) && button == 0) {
//            this.binding = !this.binding;
//        }
//    }
//
//    @Override
//    public void keyTyped(char typedChar, int key) {
//        if(this.binding && key != Keyboard.KEY_DELETE) {
//            //this.parent.mod.setKey(key);
//            this.binding = false;
//        }
//        else if(this.binding && (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_ESCAPE)) {
//            //this.parent.mod.setKey(0);
//            this.binding = false;
//        }
//    }
//
//    public boolean isMouseOnButton(int x, int y) {
//        return x > this.x && x < this.x + parent.posX && y > this.y && y < this.y + 12;
//    }
//}
