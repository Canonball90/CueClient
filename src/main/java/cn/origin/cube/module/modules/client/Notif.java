package cn.origin.cube.module.modules.client;

import cn.origin.cube.core.events.client.ModuleToggleEvent;
import cn.origin.cube.core.managers.NotificationManager;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.IntegerSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Constant
@ModuleInfo(name = "Notification", descriptions = "", category = Category.CLIENT)
public class Notif extends Module {

    public IntegerSetting speed = registerSetting("Speed", 1, 1, 10);
    public IntegerSetting time = registerSetting("Time", 1, 1, 10);

    static Notif INSTANCE = new Notif();

    @SubscribeEvent
    public void onModuleEnable(ModuleToggleEvent.Enable event){
        NotificationManager.sendMessage(event.getModule().name, ChatFormatting.GREEN + "Enabled");
    }

    @SubscribeEvent
    public void onModuleDisable(ModuleToggleEvent.Disable event){
        NotificationManager.sendMessage(event.getModule().name, ChatFormatting.RED + "Disabled");
    }


    public static Notif getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notif();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}
