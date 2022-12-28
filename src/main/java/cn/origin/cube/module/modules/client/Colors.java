package cn.origin.cube.module.modules.client;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.module.interfaces.Para;

import java.awt.*;

//ToDo make sure every color is smth like this
@Para(para = Para.ParaMode.Full)
@ModuleInfo(name = "Colors", descriptions = "", category = Category.CLIENT)
public class Colors extends Module {

    public static Colors INSTANCE;

    public Colors() {INSTANCE = this;}

    //ClickGui
    public IntegerSetting ClickGuired = registerSetting("Click Red", 25, 0, 255);
    public IntegerSetting ClickGuigreen = registerSetting("Click Green", 115, 0, 255);
    public IntegerSetting ClickGuiblue = registerSetting("Click Blue", 255, 0, 255);

    //HoleEsp
    public IntegerSetting HoleEspredOb = registerSetting("Hole Red", 25, 0, 255);
    public IntegerSetting HoleEspgreenOb = registerSetting("Hole Green", 115, 0, 255);
    public IntegerSetting HoleEspblueOb = registerSetting("Hole Blue", 255, 0, 255);
    public IntegerSetting HoleEspalphaOb = registerSetting("Hole Alpha", 255, 0, 255);

    public IntegerSetting HoleEspredBr = registerSetting("Hole Red-S", 25, 0, 255);
    public IntegerSetting HoleEspgreenBr = registerSetting("Hole Green-S", 115, 0, 255);
    public IntegerSetting HoleEspblueBr = registerSetting("Hole Blue-S", 255, 0, 255);
    public IntegerSetting HoleEspalphaBr = registerSetting("Hole Alpha-S", 255, 0, 255);

    //Literally everything else
    public IntegerSetting Everyred = registerSetting("Global Red", 25, 0, 255);
    public IntegerSetting Everygreen = registerSetting("Global Green", 115, 0, 255);
    public IntegerSetting Everyblue = registerSetting("Global Blue", 255, 0, 255);

    /*
    Clickgui
     */
    public static Color getClickGuiColor() {
        return new Color(INSTANCE.ClickGuired.getValue(), INSTANCE.ClickGuigreen.getValue(), INSTANCE.ClickGuiblue.getValue());
    }

    /*
    Hole
     */
    public static Color getHoleColorOb() {
        return new Color(INSTANCE.HoleEspredOb.getValue(), INSTANCE.HoleEspgreenOb.getValue(), INSTANCE.HoleEspblueOb.getValue(), INSTANCE.HoleEspalphaOb.getValue());
    }

    public static Color getHoleColorBr() {
        return new Color(INSTANCE.HoleEspredBr.getValue(), INSTANCE.HoleEspgreenBr.getValue(), INSTANCE.HoleEspblueBr.getValue(), INSTANCE.HoleEspalphaBr.getValue());
    }

    /*
    Global
     */
    public static Color getGlobalColor() {
        return new Color(INSTANCE.Everyred.getValue(), INSTANCE.Everygreen.getValue(), INSTANCE.Everyblue.getValue());
    }

}