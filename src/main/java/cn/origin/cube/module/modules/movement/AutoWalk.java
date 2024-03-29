package cn.origin.cube.module.modules.movement;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
@Constant(constant = false)
@ModuleInfo(name = "AutoWalk", descriptions = "Auto move forward", category = Category.MOVEMENT)
public class AutoWalk extends Module {
    @SubscribeEvent
    public void onUpdateInput(InputUpdateEvent event) {
        event.getMovementInput().moveForward = 1.0f;
    }
}

