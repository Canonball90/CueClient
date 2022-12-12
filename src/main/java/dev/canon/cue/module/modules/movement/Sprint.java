package dev.canon.cue.module.modules.movement;

import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.settings.BooleanSetting;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;

@ModuleInfo(name = "Sprint", descriptions = "Always run fast", category = Category.MOVEMENT)
public class Sprint extends Module {

    public BooleanSetting Legit = registerSetting("Legit", false);

    @Override
    public void onUpdate() {
        if (fullNullCheck() || mc.player.isElytraFlying() || mc.player.capabilities.isFlying) {
            return;
        }
        if (Legit.getValue()) {
            try {
                mc.player.setSprinting(!mc.player.collidedHorizontally && mc.player.moveForward > 0);
            } catch (Exception ignored) {
            }
        } else {
            if (mc.player.moveForward != 0.0 || mc.player.moveStrafing != 0.0) {
                mc.player.setSprinting(true);
            }
        }
    }

    @Override
    public String getHudInfo() {
        return Legit.getValue() ? "Legit" : "Normal";
    }
}
