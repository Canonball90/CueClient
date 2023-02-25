package cn.origin.cube;

import cn.origin.cube.core.managers.CommandManager;
import cn.origin.cube.core.managers.EventManager;
import cn.origin.cube.core.managers.FontManager;
import cn.origin.cube.core.managers.ModuleManager;
import cn.origin.cube.core.managers.*;
import cn.origin.cube.guis.HudEditorScreen;
import cn.origin.cube.guis.alt.manage.AltManager;
import cn.origin.cube.guis.gui.ClickGuiScreen;
import cn.origin.cube.guis.mainmenu.onGuiOpenEvent;
import cn.origin.cube.guis.otheruis.mainmenu.Shaders;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import vazkii.minetunes.config.MTConfig;
import vazkii.minetunes.key.KeyBindings;
import vazkii.minetunes.player.HUDHandler;
import vazkii.minetunes.player.ThreadMusicPlayer;
import vazkii.minetunes.playlist.PlaylistList;
import vazkii.minetunes.playlist.ThreadPlaylistCreator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

@Mod(modid = Cube.MOD_ID, name = Cube.MOD_NAME, version = Cube.MOD_VERSION)
public class Cube {
    public static final String MOD_ID = "cue";

    public static final String MOD_NAME = "Cue";

    public static final String MOD_VERSION = "3";
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
    public static RotationManager rotationManager = null;
    public static AltManager altManager;
    public static Shaders shaders;
    public static ThreadManager threadManager;
    public static PositionManager positionManager;
    public static cn.origin.cube.core.events.event.event.EventManager EVENT_BUS = new cn.origin.cube.core.events.event.event.EventManager();

    public static boolean DEBUG_MODE = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;

    public volatile static ThreadMusicPlayer musicPlayerThread;
    public volatile static ThreadPlaylistCreator playlistCreatorThread;

    public static String commandPrefix = ".";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Begin loading Cue");
        Display.setTitle(MOD_NAME + " | " + MOD_VERSION);
        Discord.startRPC();
//        KeyBindings.init();
//
//        MinecraftForge.EVENT_BUS.register(new HUDHandler());
//
//        MTConfig.findCompoundAndLoad();
//        PlaylistList.findCompoundAndLoad();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            logger.info("Loading Cue...");
            loadManagers();
            MinecraftForge.EVENT_BUS.register(new onGuiOpenEvent());
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
        rotationManager = new RotationManager();
        logger.info("Loaded RotationManager");
        AntiDumpManager.check();
        logger.info("AntiDump...");
    }

    public static void startMusicPlayerThread() {
        musicPlayerThread = new ThreadMusicPlayer();
    }

    public static void startPlaylistCreatorThread(File file, String name) {
        playlistCreatorThread = new ThreadPlaylistCreator(file, name);
    }

    public static void onShutdown() {
        Discord.stopRPC();
    }
}
