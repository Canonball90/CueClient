package cn.origin.cube.module.modules.world;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.FloatSetting;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

@Constant
@ModuleInfo(name = "BlockReach", descriptions = "", category = Category.WORLD)
public class BlockReach extends Module {

    FloatSetting reachAmount = registerSetting("Reach", 0.5f, 0f, 2f);

    @Override
    public void onEnable() {
        EntityPlayer player = mc.player;
        IAttributeInstance setBlockReachDi = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE);
        player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).applyModifier(new AttributeModifier(player.getUniqueID(), "custom_reach", reachAmount.getValue(), 1));
    }

    @Override
    public void onUpdate() {
        if (mc.player != null) {
        }
    }

    @Override
    public void onDisable() {
        mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).removeModifier(mc.player.getUniqueID());
    }
}
