package cn.origin.cube.module.modules.function;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.module.interfaces.Para;

@Constant(constant = false)
@Para(para = Para.ParaMode.Light)
@ModuleInfo(name = "BaritoneClick", descriptions = "", category = Category.FUNCTION)
public class BaritoneClick extends Module {

    @Override
    public void onEnable(){
       if(constant == false){
           mc.player.motionY = 6;
       }
    }
}
