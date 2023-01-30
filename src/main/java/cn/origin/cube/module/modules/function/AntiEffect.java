package cn.origin.cube.module.modules.function;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import net.minecraft.potion.Potion;

import java.util.Objects;

@Constant(constant = true)
@ModuleInfo(name = "AntiEffect", descriptions = "1", category = Category.FUNCTION)
public class AntiEffect extends Module {

    BooleanSetting lev = this.registerSetting("Levitate", true);
    BooleanSetting jumpB = this.registerSetting("JumpBoost", true);

    @Override
    public void onUpdate(){
        if(constant){
            if(lev.getValue() && mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionFromResourceLocation((String)"levitation")))) {
                mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation((String)"levitation"));
            }
            if(jumpB.getValue() && mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionFromResourceLocation((String)"jump_boost")))) {
                mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation((String)"jump_boost"));
            }
        }else{
            toggle();
        }
    }
}
