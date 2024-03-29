package cn.origin.cube.module.modules.function;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.StringSetting;
import cn.origin.cube.utils.client.ChatUtil;
import net.minecraft.util.text.TextComponentString;
@Constant(constant = false)
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
