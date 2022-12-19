package cn.origin.cube.module.modules.client;

import cn.origin.cube.Cube;
import cn.origin.cube.core.managers.ConfigManager;
import cn.origin.cube.event.events.player.DeathEvent;
import cn.origin.cube.guis.ClickGuiScreen;
import cn.origin.cube.guis.statistics.StatScreen;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.module.interfaces.Para;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Para(para = Para.ParaMode.Full)
@ModuleInfo(name = "Stats", descriptions = "", category = Category.CLIENT)
public class Stats extends Module {

    public void onEnable() {
        if (!this.fullNullCheck() && !(Module.mc.currentScreen instanceof ClickGuiScreen)) {
            Module.mc.displayGuiScreen(new StatScreen());
        }
        disable();
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if(event.getPlayer().isDead){

        }
    }

    public void onDisable() {
        if (!this.fullNullCheck() && Module.mc.currentScreen instanceof ClickGuiScreen) {
            Module.mc.displayGuiScreen(null);
            ConfigManager configManager = Cube.configManager;
            configManager.saveAll();
        }
    }
}
