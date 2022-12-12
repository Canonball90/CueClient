package dev.canon.cue.module.modules.function;

import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.settings.StringSetting;
import dev.canon.cue.utils.client.ChatUtil;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import net.minecraft.util.text.TextComponentString;

@ModuleInfo(name = "FakeKick", descriptions = "Disconnection form server", category = Category.FUNCTION)
public class FakeKick extends Module {

    StringSetting msg = registerSetting("KickMessage", "&c&lYou has been banned form server!");

    @Override
    public void onEnable() {
        if (!mc.isSingleplayer()) {
            mc.player.connection.getNetworkManager().closeChannel(new TextComponentString(ChatUtil.translateAlternateColorCodes(msg.getValue())));
        } else {
            ChatUtil.sendMessage("&cCouldn't use it in SinglePlayer!");
        }
        this.disable();
    }
}
