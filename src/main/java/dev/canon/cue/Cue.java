package dev.canon.cue;

import dev.canon.cue.command.CommandManager;
import dev.canon.cue.event.EventManager;
import dev.canon.cue.font.FontManager;
import dev.canon.cue.guis.ClickGuiScreen;
import dev.canon.cue.guis.HudEditorScreen;
import dev.canon.cue.guis.mainmenu.Shaders;
import dev.canon.cue.managers.CapeManager;
import dev.canon.cue.managers.ConfigManager;
import dev.canon.cue.managers.FriendManager;
import dev.canon.cue.managers.ThreadManager;
import dev.canon.cue.module.ModuleManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;

@Mod(modid = Cue.MOD_ID, name = Cue.MOD_NAME, version = Cue.MOD_VERSION)
public class Cue {
    public static final String MOD_ID = "cue";

    public static final String MOD_NAME = "Cue";

    public static final String MOD_VERSION = "0.1";
    public static final Logger logger = LogManager.getLogger("Cue");
    public static EventManager eventManager = null;
    public static FontManager fontManager = null;
    public static ClickGuiScreen clickGui = null;
    public static HudEditorScreen hudEditor = null;
    public static FriendManager friendManager = null;
    public static ModuleManager moduleManager = null;
    public static ConfigManager configManager = null;
    public static CommandManager commandManager = null;
    public static CapeManager capeManager = null;
    public static Shaders shaders;
    public static ThreadManager threadManager;

    public static String commandPrefix = ".";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Begin loading Cue");
        Display.setTitle(MOD_NAME + " | " + MOD_VERSION);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            logger.info("Loading Cue...");
            loadManagers();
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadManagers() throws IOException, FontFormatException {
        fontManager = new FontManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        eventManager = new EventManager();
        clickGui = new ClickGuiScreen();
        hudEditor = new HudEditorScreen();
        configManager = new ConfigManager();
        commandManager = new CommandManager();
        capeManager = new CapeManager();
        shaders = new Shaders();
        threadManager = new ThreadManager();
    }
}
