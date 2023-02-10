package cn.origin.cube;

import cn.origin.cube.core.managers.CommandManager;
import cn.origin.cube.core.managers.EventManager;
import cn.origin.cube.core.managers.FontManager;
import cn.origin.cube.core.managers.ModuleManager;
import cn.origin.cube.core.managers.*;
import cn.origin.cube.guis.HudEditorScreen;
import cn.origin.cube.guis.gui.ClickGuiScreen;
import cn.origin.cube.guis.otheruis.mainmenu.Shaders;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.IOException;

@Mod(modid = Cube.MOD_ID, name = Cube.MOD_NAME, version = Cube.MOD_VERSION)
public class Cube {
    public static final String MOD_ID = "cue";

    public static final String MOD_NAME = "Cue";

    public static final String MOD_VERSION = "2.28";
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
    public static TrackerManager trackerManager = null;
    public static Shaders shaders;
    public static ThreadManager threadManager;
    public static PositionManager positionManager;
    public static cn.origin.cube.core.events.event.event.EventManager EVENT_BUS = new cn.origin.cube.core.events.event.event.EventManager();

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
        logger.info("Loaded FontManager");
        friendManager = new FriendManager();
        logger.info("Loaded FriendManager");
        moduleManager = new ModuleManager();
        logger.info("Loaded ModuleManager");
        eventManager = new EventManager();
        logger.info("Loaded EventManager");
        clickGui = new ClickGuiScreen();
        logger.info("Loaded ClickGuiManager");
        hudEditor = new HudEditorScreen();
        logger.info("Loaded HudManager");
        configManager = new ConfigManager();
        logger.info("Loaded ConfigManager");
        trackerManager = new TrackerManager();
        logger.info("Loaded TrackerManager");
        commandManager = new CommandManager();
        logger.info("Loaded CommandManager");
        capeManager = new CapeManager();
        logger.info("Loaded CapeManager");
        shaders = new Shaders();
        logger.info("Loaded Shaders");
        threadManager = new ThreadManager();
        logger.info("Loaded ThreadManager");
        positionManager = new PositionManager();
        logger.info("Loaded PositionManager");
        AntiDumpManager.check();
        logger.info("AntiDump...");
    }
}
