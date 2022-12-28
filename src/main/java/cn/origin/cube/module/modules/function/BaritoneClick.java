package cn.origin.cube.module.modules.function;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.module.interfaces.Para;

@Para(para = Para.ParaMode.Light)
@ModuleInfo(name = "BaritoneClick", descriptions = "", category = Category.FUNCTION)
public class BaritoneClick extends Module {

    @Override
    public void onEnable(){
       // baritone.api.BaritoneAPI.getProvider().getPrimaryBaritone().openClick();
    }
}
