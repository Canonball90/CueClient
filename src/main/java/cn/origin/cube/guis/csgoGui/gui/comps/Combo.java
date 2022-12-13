//package cn.origin.cube.guis.csgoGui.gui.comps;
//
//import com.example.examplemod.Module.Module;
//import com.example.examplemod.Utils.RenderUtils;
//import font.FontUtils;
//import cn.origin.cube.guis.csgoGui.gui.Comp;
//import cn.origin.cube.guis.csgoGui.gui.SkeetGUI;
//import yea.bushroot.clickgui.Setting;
//
//import java.awt.*;
//
//public class Combo extends Comp {
//
//    public Combo(double x, double y, SkeetGUI parent, Module module, Setting setting) {
//        this.x = x;
//        this.y = y;
//        this.parent = parent;
//        this.module = module;
//        this.setting = setting;
//    }
//
//
//    @Override
//    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
//        super.mouseClicked(mouseX, mouseY, mouseButton);
//        if (isInside(mouseX, mouseY, parent.posX + x - 70, parent.posY + y, parent.posX + x, parent.posY + y + 10) && mouseButton == 0) {
//            int max = setting.getOptions().size();
//            if (parent.modeIndex + 1 >= max) {
//                parent.modeIndex = 0;
//            } else {
//                parent.modeIndex++;
//            }
//            setting.setValString(setting.getOptions().get(parent.modeIndex));
//        }
//    }
//
//    @Override
//    public void drawScreen(int mouseX, int mouseY) {
//        super.drawScreen(mouseX, mouseY);
//        RenderUtils.drawRect((int) (parent.posX + x - 70), (int) (parent.posY + y), (int) (parent.posX + x), (int) (parent.posY + y + 10),setting.getValBoolean() ? new Color(34, 255, 0).getRGB() : new Color(30,30,30).getRGB());
//        FontUtils.normal.drawString(setting.getName() + ": " + setting.getValString(), (int)(parent.posX + x - 69), (int)(parent.posY + y + 5), new Color(200,200,200).getRGB());
//    }
//}
